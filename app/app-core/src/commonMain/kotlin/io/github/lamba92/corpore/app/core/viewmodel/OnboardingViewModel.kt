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
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class OnboardingData(
    val selectedTrainingLevel: TrainingLevel? = null,
    val measurementUnitSystem: MeasurementUnitSystem = MeasurementUnitSystem.Metric,
    val physicalProfile: PhysicalProfile = PhysicalProfile(),
    val selectedActivities: Set<SportActivity> = emptySet(),
    val currentFitnessLevelUserInputs: CurrentFitnessLevelUserInputs = CurrentFitnessLevelUserInputs(),
) {
    @Serializable
    data class PhysicalProfile(
        val yearOfBirth: Int? = null,
        val weight: Weight = Weight.ZERO,
        val height: Length = Length.ZERO,
    )

    @Serializable
    data class CurrentFitnessLevelUserInputs(
        val gym: GymFitness = GymFitness(),
        val running: RunningFitness = RunningFitness(),
        val swimming: SwimmingFitness = SwimmingFitness(),
        val freeBody: FreeBodyFitness = FreeBodyFitness(),
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
        val knownStrokes: Set<Stroke> = setOf(Stroke.Freestyle)
    ) {
        enum class Stroke {
            Freestyle,
            Backstroke,
            Breaststroke,
            Butterfly,
        }
    }

    @Serializable
    data class FreeBodyFitness(
        val maxPushups: Int? = null,
        val wallSitHold: Duration? = null,
        val canPlank30Sec: Boolean = false,
        val hasTrainedBefore: Boolean = false,
        val sessionsPerWeek: Int? = null,
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
    data class MeasurementSystemSelected(val measurementUnitSystem: MeasurementUnitSystem) : OnboardingDataUpdateEvent

    sealed interface PhysicalProfile : OnboardingDataUpdateEvent {
        data class YearOfBirthSelected(val year: Int) : PhysicalProfile

        data class WeightSelected(val weight: Weight) : PhysicalProfile

        data class HeightSelected(val height: Length) : PhysicalProfile

    }

    sealed interface ActivitiesSelection : OnboardingDataUpdateEvent {
        data class ActivityAdded(val activities: List<SportActivity>) : ActivitiesSelection

        data class ActivityRemoved(val activities: List<SportActivity>) : ActivitiesSelection
    }

    sealed interface CurrentFitnessLevelInput : OnboardingDataUpdateEvent {
        sealed interface Gym : CurrentFitnessLevelInput {
            data class BenchPress1RMSelected(val weight: Weight) : Gym
            data class Squat1RMSelected(val weight: Weight) : Gym
            data class Deadlift1RMSelected(val weight: Weight) : Gym
        }

        sealed interface Running : CurrentFitnessLevelInput {
            data class DistanceIn30MinsSelected(val length: Length) : Running
        }

        sealed interface Swimming : CurrentFitnessLevelInput {
            data class FreestyleDistance15MinSelected(val length: Length) : Swimming
            data class KnownStrokesAdded(val stroke: OnboardingData.SwimmingFitness.Stroke) : Swimming
            data class KnownStrokesRemoved(val stroke: OnboardingData.SwimmingFitness.Stroke) : Swimming
        }

        sealed interface FreeBody : CurrentFitnessLevelInput {
            data class MaxPushupsSelected(val maxPushups: Int) : FreeBody
            data class WallSitHoldSelected(val duration: Duration) : FreeBody
            data class CanPlank30SecSelected(val canPlank: Boolean) : FreeBody
            data class HasTrainedBeforeSelected(val hasTrainedBefore: Boolean) : FreeBody
            data class SessionsPerWeekSelected(val sessionsPerWeek: Int) : FreeBody
        }
    }
}

enum class OnboardingStep {
    TrainingLevelSelection,
    PhysicalProfile,
    ActivitiesSelection,
    CurrentFitnessLevelInput,
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
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput -> update(event)
        }
    }

    fun update(event: OnboardingDataUpdateEvent.PhysicalProfile) {
        when (event) {
            is OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile = onboardingDataStateFlow.value.physicalProfile.copy(
                            height = event.height,
                        )
                    )
            is OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile = onboardingDataStateFlow.value.physicalProfile.copy(
                            weight = event.weight,
                        )
                    )
            is OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        physicalProfile = onboardingDataStateFlow.value.physicalProfile.copy(
                            yearOfBirth = event.year,
                        )
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

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevelInput) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Running -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming -> update(event)
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody -> update(event)
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.BenchPress1RMSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            gym = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.gym.copy(
                                benchPress1RM = event.weight,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.Squat1RMSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            gym = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.gym.copy(
                                squat1RM = event.weight,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.Deadlift1RMSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            gym = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.gym.copy(
                                deadlift1RM = event.weight,
                            )
                        )
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Running) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Running.DistanceIn30MinsSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            running = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.running.copy(
                                distanceIn30Mins = event.length,
                            )
                        )
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.FreestyleDistance15MinSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            swimming = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.swimming.copy(
                                freestyleDistance15Min = event.length,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.KnownStrokesAdded ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            swimming = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.swimming.copy(
                                knownStrokes = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.swimming.knownStrokes + event.stroke,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.KnownStrokesRemoved ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            swimming = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.swimming.copy(
                                knownStrokes = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.swimming.knownStrokes - event.stroke,
                            )
                        )
                    )
        }
    }

    fun update(event: OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody) {
        when (event) {
            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody.MaxPushupsSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            freeBody = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.freeBody.copy(
                                maxPushups = event.maxPushups,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody.WallSitHoldSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            freeBody = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.freeBody.copy(
                                wallSitHold = event.duration,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody.CanPlank30SecSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            freeBody = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.freeBody.copy(
                                canPlank30Sec = event.canPlank,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody.HasTrainedBeforeSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            freeBody = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.freeBody.copy(
                                hasTrainedBefore = event.hasTrainedBefore,
                            )
                        )
                    )

            is OnboardingDataUpdateEvent.CurrentFitnessLevelInput.FreeBody.SessionsPerWeekSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        currentFitnessLevelUserInputs = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.copy(
                            freeBody = onboardingDataStateFlow.value.currentFitnessLevelUserInputs.freeBody.copy(
                                sessionsPerWeek = event.sessionsPerWeek,
                            )
                        )
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
            data.physicalProfile.yearOfBirth != null &&
                data.physicalProfile.weight != Weight.ZERO &&
                data.physicalProfile.height != Length.ZERO

        OnboardingStep.ActivitiesSelection -> data.selectedActivities.isNotEmpty()
        OnboardingStep.CurrentFitnessLevelInput -> true
        OnboardingStep.ActivitiesRotationFrequency -> true
    }
