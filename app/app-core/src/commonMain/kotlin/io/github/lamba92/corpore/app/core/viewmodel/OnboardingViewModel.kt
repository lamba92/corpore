package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.ui.onboarding.TrainingLevel
import io.github.lamba92.corpore.app.core.usecase.execute
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.Weight
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class OnboardingData(
    val selectedTrainingLevel: TrainingLevel? = null,
    val measurementUnitSystem: MeasurementUnitSystem = MeasurementUnitSystem.Metric,
    val physicalProfile: PhysicalProfile = PhysicalProfile(),
    val selectedActivities: Set<SportActivity> = emptySet(),
    val currentFitnessLevel: CurrentFitnessLevelUserInputs = CurrentFitnessLevelUserInputs(),
) {
    @Serializable
    data class PhysicalProfile(
        val yearOfBirth: Int = Clock.System.now().toLocalDateTime(TimeZone.UTC).year - 16,
        val weight: Weight = Weight.ZERO,
        val height: Length = Length.ZERO,
    )

    @Serializable
    data class CurrentFitnessLevelUserInputs(
        val gym: GymFitness = GymFitness(),
        val running: RunningFitness = RunningFitness(),
        val swimming: SwimmingFitness = SwimmingFitness(),
        val calisthenics: CalisthenicsFitness = CalisthenicsFitness(),
    )

    @Serializable
    data class GymFitness(
        val benchPress1RM: Weight = Weight.ZERO,
        val squat1RM: Weight = Weight.ZERO,
        val deadlift1RM: Weight = Weight.ZERO,
    )

    @Serializable
    data class RunningFitness(
        val distanceIn30Mins: Length = Length.ZERO,
    )

    @Serializable
    data class SwimmingFitness(
        val freestyleDistance15Min: Length = Length.ZERO,
        val knownStrokes: Set<Stroke> = setOf(Stroke.Freestyle),
    ) {
        enum class Stroke {
            Freestyle,
            Backstroke,
            Breaststroke,
            Butterfly,
        }
    }

    @Serializable
    data class CalisthenicsFitness(
        val hasTrainedBefore: Boolean = false,
        val maxPushups: Int = 0,
        val wallSitHold: Duration = Duration.ZERO,
        val canPlank30Sec: Boolean = false,
    )
}

enum class MeasurementUnitSystem {
    Metric,
    Imperial,
}

enum class SportActivity {
    Gym,
    Running,
    Swimming,
    FreeBody,
}

sealed interface OnboardingDataUpdateEvent {
    data class TrainingLevelSelected(val trainingLevel: TrainingLevel) : OnboardingDataUpdateEvent

    data class MeasurementSystemSelected(val measurementUnitSystem: MeasurementUnitSystem) :
        OnboardingDataUpdateEvent

    sealed interface PhysicalProfile : OnboardingDataUpdateEvent {
        data class YearOfBirthSelected(val year: Int) : PhysicalProfile

        data class WeightSelected(val weight: Weight) : PhysicalProfile

        data class HeightSelected(val height: Length) : PhysicalProfile
    }

    sealed interface ActivitiesSelection : OnboardingDataUpdateEvent {
        data class ActivityAdded(val activities: List<SportActivity>) : ActivitiesSelection

        data class ActivityRemoved(val activities: List<SportActivity>) : ActivitiesSelection
    }

    sealed interface CurrentFitnessLevel : OnboardingDataUpdateEvent {
        sealed interface Gym : CurrentFitnessLevel {
            data class BenchPress1RMChange(val weight: Weight) : Gym

            data class Squat1RMChange(val weight: Weight) : Gym

            data class Deadlift1RMChange(val weight: Weight) : Gym
        }

        sealed interface Running : CurrentFitnessLevel {
            data class DistanceIn30MinsChange(val length: Length) : Running
        }

        sealed interface Swimming : CurrentFitnessLevel {
            data class FreestyleDistance15MinChange(val length: Length) : Swimming

            data class KnownStrokesAdded(val stroke: OnboardingData.SwimmingFitness.Stroke) :
                Swimming

            data class KnownStrokesRemoved(val stroke: OnboardingData.SwimmingFitness.Stroke) :
                Swimming
        }

        sealed interface FreeBody : CurrentFitnessLevel {
            data object HasTrainedBeforeToggle : FreeBody

            data class MaxPushupsChange(val maxPushups: Int) : FreeBody

            data class WallSitHoldChange(val duration: Duration) : FreeBody

            data object CanPlank30SecToggle : FreeBody
        }
    }
}

enum class OnboardingStep {
    TrainingLevelSelection,
    PhysicalProfile,
    ActivitiesSelection,
    CurrentFitnessLevel,
    ActivitiesRotationFrequency,
}

class OnboardingViewModel(
    private val logoutUseCase: LogoutUseCase,
    authRepository: AuthRepository,
) : ViewModel() {
    val isLoggedInStateFlow =
        authRepository
            .authSession
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = true,
            )

    private val _onboardingCompleteSharedFlow =
        MutableSharedFlow<Unit>()

    val onboardingCompleteSharedFlow =
        _onboardingCompleteSharedFlow.asSharedFlow()

    private val _isLoggingOutStateFlow =
        MutableStateFlow(false)

    val isLoggingOutStateFlow =
        _isLoggingOutStateFlow.asStateFlow()

    val onboardingDataStateFlow =
        MutableStateFlow(OnboardingData())

    private val _currentOnboardingStepStateFlow =
        MutableStateFlow(OnboardingStep.TrainingLevelSelection)

    val currentOnboardingStepStateFlow =
        _currentOnboardingStepStateFlow.asStateFlow()

    val canGoNextStateFlow =
        combine(
            onboardingDataStateFlow,
            _currentOnboardingStepStateFlow,
        ) { onboardingData, currentOnboardingStep ->
            currentOnboardingStep.canGoNext(onboardingData)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false,
            )

    fun logout() {
        _isLoggingOutStateFlow.value = true
        viewModelScope.launch {
            logoutUseCase.execute()
            _isLoggingOutStateFlow.value = false
        }
    }

    fun update(event: OnboardingDataUpdateEvent) {
        when (event) {
            is OnboardingDataUpdateEvent.TrainingLevelSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        selectedTrainingLevel = event.trainingLevel,
                    )

            is OnboardingDataUpdateEvent.MeasurementSystemSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        measurementUnitSystem = event.measurementUnitSystem,
                    )

            is OnboardingDataUpdateEvent.PhysicalProfile -> update(event)
            is OnboardingDataUpdateEvent.ActivitiesSelection -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevel -> update(event)
        }
    }

    fun update(event: OnboardingDataUpdateEvent.PhysicalProfile) {
        when (event) {
            is OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile =
                            onboardingDataStateFlow.value.physicalProfile.copy(
                                height = event.height,
                            ),
                    )

            is OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile =
                            onboardingDataStateFlow.value.physicalProfile.copy(
                                weight = event.weight,
                            ),
                    )

            is OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile =
                            onboardingDataStateFlow.value.physicalProfile.copy(
                                yearOfBirth = event.year,
                            ),
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.ActivitiesSelection) {
        when (event) {
            is OnboardingDataUpdateEvent.ActivitiesSelection.ActivityAdded ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        selectedActivities = onboardingDataStateFlow.value.selectedActivities + event.activities,
                    )

            is OnboardingDataUpdateEvent.ActivitiesSelection.ActivityRemoved ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        selectedActivities = onboardingDataStateFlow.value.selectedActivities - event.activities,
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevel) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Gym -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Running -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Swimming -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody -> update(event)
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevel.Gym) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Gym.BenchPress1RMChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                gym =
                                    onboardingDataStateFlow.value.currentFitnessLevel.gym.copy(
                                        benchPress1RM = event.weight,
                                    ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Gym.Squat1RMChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                gym =
                                    onboardingDataStateFlow.value.currentFitnessLevel.gym.copy(
                                        squat1RM = event.weight,
                                    ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Gym.Deadlift1RMChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                gym =
                                    onboardingDataStateFlow.value.currentFitnessLevel.gym.copy(
                                        deadlift1RM = event.weight,
                                    ),
                            ),
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevel.Running) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Running.DistanceIn30MinsChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                running =
                                    onboardingDataStateFlow.value.currentFitnessLevel.running.copy(
                                        distanceIn30Mins = event.length,
                                    ),
                            ),
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevel.Swimming) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Swimming.FreestyleDistance15MinChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                swimming =
                                    onboardingDataStateFlow.value.currentFitnessLevel.swimming.copy(
                                        freestyleDistance15Min = event.length,
                                    ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Swimming.KnownStrokesAdded ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                swimming =
                                    onboardingDataStateFlow
                                        .value
                                        .currentFitnessLevel
                                        .swimming
                                        .copy(
                                            knownStrokes =
                                                onboardingDataStateFlow
                                                    .value
                                                    .currentFitnessLevel
                                                    .swimming
                                                    .knownStrokes + event.stroke,
                                        ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.Swimming.KnownStrokesRemoved ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                swimming =
                                    onboardingDataStateFlow
                                        .value
                                        .currentFitnessLevel
                                        .swimming
                                        .copy(
                                            knownStrokes =
                                                onboardingDataStateFlow
                                                    .value
                                                    .currentFitnessLevel
                                                    .swimming
                                                    .knownStrokes - event.stroke,
                                        ),
                            ),
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody.MaxPushupsChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                calisthenics =
                                    onboardingDataStateFlow.value.currentFitnessLevel.calisthenics.copy(
                                        maxPushups = event.maxPushups,
                                    ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody.WallSitHoldChange ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                calisthenics =
                                    onboardingDataStateFlow.value.currentFitnessLevel.calisthenics.copy(
                                        wallSitHold = event.duration,
                                    ),
                            ),
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody.CanPlank30SecToggle ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                calisthenics =
                                    onboardingDataStateFlow.value.currentFitnessLevel.calisthenics.copy(
                                        canPlank30Sec =
                                            !onboardingDataStateFlow
                                                .value
                                                .currentFitnessLevel
                                                .calisthenics
                                                .canPlank30Sec,
                                    ),
                            ),
                    )

            OnboardingDataUpdateEvent.CurrentFitnessLevel.FreeBody.HasTrainedBeforeToggle ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevel =
                            onboardingDataStateFlow.value.currentFitnessLevel.copy(
                                calisthenics =
                                    onboardingDataStateFlow.value.currentFitnessLevel.calisthenics.copy(
                                        hasTrainedBefore =
                                            !onboardingDataStateFlow
                                                .value
                                                .currentFitnessLevel
                                                .calisthenics
                                                .hasTrainedBefore,
                                    ),
                            ),
                    )
        }
    }

    fun onBackClick() {
        when {
            _currentOnboardingStepStateFlow.value.ordinal != 0 ->
                navigate(_currentOnboardingStepStateFlow.value, -1)

            else -> logout()
        }
    }

    fun onNextClick() {
        val currentStep = _currentOnboardingStepStateFlow.value
        if (!currentStep.canGoNext(onboardingDataStateFlow.value)) return
        when {
            currentStep.ordinal == OnboardingStep.entries.lastIndex ->
                _onboardingCompleteSharedFlow.tryEmit(Unit)

            else -> navigate(currentStep, 1)
        }
    }

    private fun navigate(
        currentStep: OnboardingStep,
        steps: Int,
    ) {
        _currentOnboardingStepStateFlow.value = OnboardingStep.entries[currentStep.ordinal + steps]
    }
}

fun OnboardingStep.canGoNext(data: OnboardingData) =
    when (this) {
        OnboardingStep.TrainingLevelSelection -> data.selectedTrainingLevel != null
        OnboardingStep.PhysicalProfile ->
            data.physicalProfile.weight != Weight.ZERO &&
                data.physicalProfile.height != Length.ZERO

        OnboardingStep.ActivitiesSelection -> data.selectedActivities.isNotEmpty()
        OnboardingStep.CurrentFitnessLevel -> validateCurrentFitnessLevel(data)
        OnboardingStep.ActivitiesRotationFrequency -> true
    }

fun validateCurrentFitnessLevel(data: OnboardingData): Boolean {
    val isGymValid =
        when (SportActivity.Gym) {
            in data.selectedActivities ->
                data.currentFitnessLevel.gym.benchPress1RM != Weight.ZERO &&
                    data.currentFitnessLevel.gym.squat1RM != Weight.ZERO &&
                    data.currentFitnessLevel.gym.deadlift1RM != Weight.ZERO
            else -> true
        }

    val isRunningValid =
        when (SportActivity.Running) {
            in data.selectedActivities ->
                data.currentFitnessLevel.running.distanceIn30Mins != Length.ZERO
            else -> true
        }

    val isSwimmingValid =
        when (SportActivity.Swimming) {
            in data.selectedActivities ->
                data.currentFitnessLevel.swimming.freestyleDistance15Min != Length.ZERO
            else -> true
        }

    val isCalisthenicsValid =
        when (SportActivity.FreeBody) {
            in data.selectedActivities ->
                when {
                    data.currentFitnessLevel.calisthenics.hasTrainedBefore ->
                        data.currentFitnessLevel.calisthenics.maxPushups != 0 &&
                            data.currentFitnessLevel.calisthenics.wallSitHold != Duration.ZERO
                    else -> true
                }
            else -> true
        }

    return isGymValid && isRunningValid && isSwimmingValid && isCalisthenicsValid
}
