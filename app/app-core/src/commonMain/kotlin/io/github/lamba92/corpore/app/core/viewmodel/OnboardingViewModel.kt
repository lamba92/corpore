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
import kotlinx.coroutines.flow.update
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
    val fitnessLevelProfile: FitnessLevelProfile = FitnessLevelProfile(),
    val activitiesRotationFrequency: RotationFrequency = RotationFrequency.Weekly,
) {
    enum class RotationFrequency {
        Weekly,
        BiWeekly,
        Monthly,
    }

    @Serializable
    data class PhysicalProfile(
        val yearOfBirth: Int = Clock.System.now().toLocalDateTime(TimeZone.UTC).year - 16,
        val weight: Weight = Weight.ZERO,
        val height: Length = Length.ZERO,
    )

    @Serializable
    data class FitnessLevelProfile(
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

    sealed interface FitnessLevelProfile : OnboardingDataUpdateEvent {
        sealed interface Gym : FitnessLevelProfile {
            data class BenchPress1RMChange(val weight: Weight) : Gym

            data class Squat1RMChange(val weight: Weight) : Gym

            data class Deadlift1RMChange(val weight: Weight) : Gym
        }

        sealed interface Running : FitnessLevelProfile {
            data class DistanceIn30MinsChange(val length: Length) : Running
        }

        sealed interface Swimming : FitnessLevelProfile {
            data class FreestyleDistance15MinChange(val length: Length) : Swimming

            data class KnownStrokesAdded(val stroke: OnboardingData.SwimmingFitness.Stroke) :
                Swimming

            data class KnownStrokesRemoved(val stroke: OnboardingData.SwimmingFitness.Stroke) :
                Swimming
        }

        sealed interface FreeBody : FitnessLevelProfile {
            data object HasTrainedBeforeToggle : FreeBody

            data class MaxPushupsChange(val maxPushups: Int) : FreeBody

            data class WallSitHoldChange(val duration: Duration) : FreeBody

            data object CanPlank30SecToggle : FreeBody
        }
    }

    data class ActivitiesRotationFrequencySelected(val frequency: OnboardingData.RotationFrequency) :
        OnboardingDataUpdateEvent
}

enum class OnboardingStep {
    TrainingLevelSelection,
    PhysicalProfile,
    ActivitiesSelection,
    FitnessLevelProfile,
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
                onboardingDataStateFlow.update { it.copy(selectedTrainingLevel = event.trainingLevel) }

            is OnboardingDataUpdateEvent.MeasurementSystemSelected ->
                onboardingDataStateFlow.update { it.copy(measurementUnitSystem = event.measurementUnitSystem) }

            is OnboardingDataUpdateEvent.PhysicalProfile -> update(event)
            is OnboardingDataUpdateEvent.ActivitiesSelection -> update(event)
            is OnboardingDataUpdateEvent.FitnessLevelProfile -> update(event)
            is OnboardingDataUpdateEvent.ActivitiesRotationFrequencySelected ->
                onboardingDataStateFlow.update { it.copy(activitiesRotationFrequency = event.frequency) }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.PhysicalProfile) {
        when (event) {
            is OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected ->
                onboardingDataStateFlow.update { it.copy(physicalProfile = it.physicalProfile.copy(height = event.height)) }

            is OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected ->
                onboardingDataStateFlow.update { it.copy(physicalProfile = it.physicalProfile.copy(weight = event.weight)) }

            is OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected ->
                onboardingDataStateFlow.update { it.copy(physicalProfile = it.physicalProfile.copy(yearOfBirth = event.year)) }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.ActivitiesSelection) {
        when (event) {
            is OnboardingDataUpdateEvent.ActivitiesSelection.ActivityAdded ->
                onboardingDataStateFlow.update {
                    it.copy(selectedActivities = it.selectedActivities + event.activities)
                }

            is OnboardingDataUpdateEvent.ActivitiesSelection.ActivityRemoved ->
                onboardingDataStateFlow.update {
                    it.copy(selectedActivities = it.selectedActivities - event.activities.toSet())
                }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.FitnessLevelProfile) {
        when (event) {
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Gym -> update(event)
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Running -> update(event)
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Swimming -> update(event)
            is OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody -> update(event)
        }
    }

    fun update(event: OnboardingDataUpdateEvent.FitnessLevelProfile.Gym) {
        when (event) {
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Gym.BenchPress1RMChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(benchPress1RM = event.weight),
                            ),
                    )
                }

            is OnboardingDataUpdateEvent.FitnessLevelProfile.Gym.Squat1RMChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(squat1RM = event.weight),
                            ),
                    )
                }

            is OnboardingDataUpdateEvent.FitnessLevelProfile.Gym.Deadlift1RMChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(deadlift1RM = event.weight),
                            ),
                    )
                }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.FitnessLevelProfile.Running) {
        when (event) {
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Running.DistanceIn30MinsChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                running = it.fitnessLevelProfile.running.copy(distanceIn30Mins = event.length),
                            ),
                    )
                }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.FitnessLevelProfile.Swimming) {
        when (event) {
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Swimming.FreestyleDistance15MinChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                swimming = it.fitnessLevelProfile.swimming.copy(freestyleDistance15Min = event.length),
                            ),
                    )
                }
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Swimming.KnownStrokesAdded ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                swimming =
                                    it.fitnessLevelProfile.swimming.copy(
                                        knownStrokes = it.fitnessLevelProfile.swimming.knownStrokes + event.stroke,
                                    ),
                            ),
                    )
                }
            is OnboardingDataUpdateEvent.FitnessLevelProfile.Swimming.KnownStrokesRemoved ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                swimming =
                                    it.fitnessLevelProfile.swimming.copy(
                                        knownStrokes = it.fitnessLevelProfile.swimming.knownStrokes - event.stroke,
                                    ),
                            ),
                    )
                }
        }
    }

    fun update(event: OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody) {
        when (event) {
            is OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody.HasTrainedBeforeToggle ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics =
                                    it.fitnessLevelProfile.calisthenics.copy(
                                        hasTrainedBefore = !it.fitnessLevelProfile.calisthenics.hasTrainedBefore,
                                    ),
                            ),
                    )
                }
            is OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody.MaxPushupsChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics = it.fitnessLevelProfile.calisthenics.copy(maxPushups = event.maxPushups),
                            ),
                    )
                }
            is OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody.WallSitHoldChange ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics = it.fitnessLevelProfile.calisthenics.copy(wallSitHold = event.duration),
                            ),
                    )
                }
            is OnboardingDataUpdateEvent.FitnessLevelProfile.FreeBody.CanPlank30SecToggle ->
                onboardingDataStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics =
                                    it.fitnessLevelProfile.calisthenics.copy(
                                        canPlank30Sec = !it.fitnessLevelProfile.calisthenics.canPlank30Sec,
                                    ),
                            ),
                    )
                }
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
        OnboardingStep.FitnessLevelProfile -> validateCurrentFitnessLevel(data)
        OnboardingStep.ActivitiesRotationFrequency -> true
    }

fun validateCurrentFitnessLevel(data: OnboardingData): Boolean {
    val isGymValid =
        when (SportActivity.Gym) {
            in data.selectedActivities ->
                data.fitnessLevelProfile.gym.benchPress1RM != Weight.ZERO &&
                    data.fitnessLevelProfile.gym.squat1RM != Weight.ZERO &&
                    data.fitnessLevelProfile.gym.deadlift1RM != Weight.ZERO
            else -> true
        }

    val isRunningValid =
        when (SportActivity.Running) {
            in data.selectedActivities ->
                data.fitnessLevelProfile.running.distanceIn30Mins != Length.ZERO
            else -> true
        }

    val isSwimmingValid =
        when (SportActivity.Swimming) {
            in data.selectedActivities ->
                data.fitnessLevelProfile.swimming.freestyleDistance15Min != Length.ZERO
            else -> true
        }

    val isCalisthenicsValid =
        when (SportActivity.FreeBody) {
            in data.selectedActivities ->
                when {
                    data.fitnessLevelProfile.calisthenics.hasTrainedBefore ->
                        data.fitnessLevelProfile.calisthenics.maxPushups != 0 &&
                            data.fitnessLevelProfile.calisthenics.wallSitHold != Duration.ZERO
                    else -> true
                }
            else -> true
        }

    return isGymValid && isRunningValid && isSwimmingValid && isCalisthenicsValid
}
