@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthService
import io.github.lamba92.corpore.app.core.ui.onboarding.content.TrainingLevel
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.LengthUnit
import io.github.lamba92.corpore.common.core.units.Weight
import io.github.lamba92.corpore.common.core.units.WeightUnit
import io.github.lamba92.corpore.common.core.usecase.execute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Serializable
data class OnboardingState(
    val trainingPreferences: TrainingPreferences = TrainingPreferences(),
    val currentStep: OnboardingStep = OnboardingStep.TrainingLevelSelection,
    val canGoNext: Boolean = false,
    val isLoggingOut: Boolean = false,
) {
    companion object {
        val INITIAL = OnboardingState()
    }
}

@Serializable
data class OnboardingState2(
    val weightUnit: WeightUnit = WeightUnit.Grams,
    val lengthUnit: LengthUnit = LengthUnit.Meters,
    val trainingLevelSelection: TrainingLevelSelectionStep,
    val physicalProfile: PhysicalProfileStep,
    val activitiesSelection: ActivitiesSelectionStep,
    val fitnessLevelProfile: FitnessLevelProfileStep,
    val rotationFrequency: RotationFrequencyStep
) {

    companion object {
        val INITIAL = OnboardingState2(
            trainingLevelSelection = TrainingLevelSelectionStep(),
            physicalProfile = PhysicalProfileStep(),
            activitiesSelection = ActivitiesSelectionStep(),
            fitnessLevelProfile = FitnessLevelProfileStep(),
            rotationFrequency = RotationFrequencyStep()
        )
    }

    interface OnboardingStep {
        val isValid: Boolean
    }

    @Serializable
    data class TrainingLevelSelectionStep(
        val level: TrainingLevel? = null
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = level != null
    }

    @Serializable
    data class PhysicalProfileStep(
        val yearOfBirth: Int? = null,
        val weight: Weight = Weight.ZERO,
        val height: Length = Length.ZERO
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = yearOfBirth != 0 && weight != Weight.ZERO && height != Length.ZERO
    }

    @Serializable
    data class ActivitiesSelectionStep(
        val activities: Set<SportActivity> = emptySet()
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = activities.isNotEmpty()

        enum class SportActivity {
            Gym,
            Running,
            Swimming,
            FreeBody,
        }
    }

    @Serializable
    data class FitnessLevelProfileStep(
        val gym: Gym = Gym(),
        val running: Running = Running(),
        val swimming: Swimming = Swimming(),
        val calisthenics: Calisthenics = Calisthenics(),
    ) {

        @Serializable
        data class Gym(
            val hasTrainedBefore: Boolean = false,
            val benchPress1RM: Weight = Weight.ZERO,
            val squat1RM: Weight = Weight.ZERO,
            val deadlift1RM: Weight = Weight.ZERO,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() = when {
                    hasTrainedBefore -> benchPress1RM != Weight.ZERO && squat1RM != Weight.ZERO && deadlift1RM != Weight.ZERO
                    else -> true
                }
        }

        @Serializable
        data class Running(
            val hasTrainedBefore: Boolean = false,
            val distanceIn30Mins: Length = Length.ZERO,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() = when {
                    hasTrainedBefore -> distanceIn30Mins != Length.ZERO
                    else -> true
                }
        }

        @Serializable
        data class Swimming(
            val hasTrainedBefore: Boolean = false,
            val freestyleDistance15Min: Length = Length.ZERO,
            val knownStrokes: Set<Strokes> = setOf(Strokes.Freestyle),
        ) : OnboardingStep {
            enum class Strokes {
                Freestyle,
                Backstroke,
                Breaststroke,
                Butterfly,
            }

            override val isValid: Boolean
                get() = when {
                    hasTrainedBefore -> freestyleDistance15Min != Length.ZERO
                    else -> true
                }
        }

        @Serializable
        data class Calisthenics(
            val hasTrainedBefore: Boolean = false,
            val maxPushups: Int = 0,
            val wallSitHold: Duration = Duration.ZERO,
            val canPlank30Sec: Boolean = false,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() = when {
                    hasTrainedBefore -> maxPushups != 0 && wallSitHold != Duration.ZERO
                    else -> true
                }
        }
    }

    @Serializable
    data class RotationFrequencyStep(
        val frequency: RotationFrequency? = null
    ) : OnboardingStep {

        override val isValid: Boolean
            get() = frequency != null

        enum class RotationFrequency {
            Weekly,
            BiWeekly,
            Monthly,
        }
    }

}

@Serializable
sealed interface OnboardingEffects {

    @Serializable
    object OnboardingComplete : OnboardingEffects

    @Serializable
    object Logout : OnboardingEffects

}

@Serializable
data class TrainingPreferences(
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
        val yearOfBirth: Int =
            Clock
                .System
                .now()
                .toLocalDateTime(TimeZone.UTC)
                .year - 16,
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

sealed interface OnboardingEvent {
    data class TrainingLevelSelected(
        val trainingLevel: TrainingLevel,
    ) : OnboardingEvent

    data class MeasurementSystemSelected(
        val measurementUnitSystem: MeasurementUnitSystem,
    ) : OnboardingEvent

    sealed interface PhysicalProfile : OnboardingEvent {
        data class YearOfBirthSelected(
            val year: Int,
        ) : PhysicalProfile

        data class WeightSelected(
            val weight: Weight,
        ) : PhysicalProfile

        data class HeightSelected(
            val height: Length,
        ) : PhysicalProfile
    }

    sealed interface ActivitiesSelection : OnboardingEvent {
        data class ActivityAdded(
            val activities: List<SportActivity>,
        ) : ActivitiesSelection

        data class ActivityRemoved(
            val activities: List<SportActivity>,
        ) : ActivitiesSelection
    }

    sealed interface FitnessLevelProfile : OnboardingEvent {
        sealed interface Gym : FitnessLevelProfile {
            data class BenchPress1RMChange(
                val weight: Weight,
            ) : Gym

            data class Squat1RMChange(
                val weight: Weight,
            ) : Gym

            data class Deadlift1RMChange(
                val weight: Weight,
            ) : Gym
        }

        sealed interface Running : FitnessLevelProfile {
            data class DistanceIn30MinsChange(
                val length: Length,
            ) : Running
        }

        sealed interface Swimming : FitnessLevelProfile {
            data class FreestyleDistance15MinChange(
                val length: Length,
            ) : Swimming

            data class KnownStrokesAdded(
                val stroke: TrainingPreferences.SwimmingFitness.Stroke,
            ) : Swimming

            data class KnownStrokesRemoved(
                val stroke: TrainingPreferences.SwimmingFitness.Stroke,
            ) : Swimming
        }

        sealed interface FreeBody : FitnessLevelProfile {
            data object HasTrainedBeforeToggle : FreeBody

            data class MaxPushupsChange(
                val maxPushups: Int,
            ) : FreeBody

            data class WallSitHoldChange(
                val duration: Duration,
            ) : FreeBody

            data object CanPlank30SecToggle : FreeBody
        }
    }

    data class ActivitiesRotationFrequencySelected(
        val frequency: TrainingPreferences.RotationFrequency,
    ) : OnboardingEvent
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
    authService: AuthService,
) : ViewModel(), MVIViewModel<OnboardingState2, OnboardingEffects, OnboardingEvent> {

    private val _state = MutableStateFlow(OnboardingState2.INITIAL)
    override val state: StateFlow<OnboardingState2> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingEffects>()
    override val effects: SharedFlow<OnboardingEffects> = _effects.asSharedFlow()

    init {
        authService
            .authSession
            .map { it != null }
            .onEach { _effects.emit(OnboardingEffects.Logout) }
            .launchIn(viewModelScope)
    }

//    private val canGoNextStateFlow =
//        combine(
//            trainingPreferencesStateFlow,
//            _currentOnboardingStepStateFlow,
//        ) { onboardingData, currentOnboardingStep ->
//            currentOnboardingStep.canGoNext(onboardingData)
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue = false,
//        )

    private fun logout() {
        _state.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch {
            logoutUseCase.execute()
        }
    }

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.TrainingLevelSelected -> onUpdate(event)
            is OnboardingEvent.MeasurementSystemSelected -> onUpdate(event)
            is OnboardingEvent.PhysicalProfile -> onEvent(event)
            is OnboardingEvent.ActivitiesSelection -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile -> onEvent(event)
            is OnboardingEvent.ActivitiesRotationFrequencySelected ->
                _state.update {
                    it.copy(
                        trainingPreferences = it.trainingPreferences.copy(
                            activitiesRotationFrequency = event.frequency
                        )
                    )
                }
        }
    }

    private fun onUpdate(event: OnboardingEvent.MeasurementSystemSelected) {
        _state.update {
            it.update(trainingPreferences = it.trainingPreferences.copy(measurementUnitSystem = event.measurementUnitSystem))
        }
    }

    private fun onUpdate(event: OnboardingEvent.TrainingLevelSelected) {
        _state.update {
            it.copy()
        }
    }

    private fun onEvent(event: OnboardingEvent.PhysicalProfile) {
        when (event) {
            is OnboardingEvent.PhysicalProfile.HeightSelected ->
                _state.update {
                    it.update(
                        trainingPreferences =
                            it.trainingPreferences.copy(
                                physicalProfile = it.trainingPreferences.physicalProfile.copy(
                                    height = event.height
                                )
                            )
                    )
                }

            is OnboardingEvent.PhysicalProfile.WeightSelected ->
                _state.update {
                    it.update(
                        trainingPreferences =
                            it.trainingPreferences.copy(
                                physicalProfile = it.trainingPreferences.physicalProfile.copy(
                                    weight = event.weight
                                )
                            )
                    )
                }

            is OnboardingEvent.PhysicalProfile.YearOfBirthSelected ->
                _state.update {
                    it.update(
                        trainingPreferences =
                            it.trainingPreferences.copy(
                                physicalProfile = it.trainingPreferences.physicalProfile.copy(
                                    yearOfBirth = event.year
                                )
                            )
                    )
                }
        }
    }

    fun onEvent(event: OnboardingEvent.ActivitiesSelection) {
        when (event) {
            is OnboardingEvent.ActivitiesSelection.ActivityAdded ->
                _state.update {
                    it.update(trainingPreferences = it.trainingPreferences.copy(selectedActivities = it.trainingPreferences.selectedActivities + event.activities.toSet()))
                }

            is OnboardingEvent.ActivitiesSelection.ActivityRemoved ->
                trainingPreferencesStateFlow.update {
                    it.copy(selectedActivities = it.selectedActivities - event.activities.toSet())
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Gym -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.Running -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.Swimming -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.FreeBody -> onEvent(event)
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Gym) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Gym.BenchPress1RMChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(benchPress1RM = event.weight),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Gym.Squat1RMChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(squat1RM = event.weight),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Gym.Deadlift1RMChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                gym = it.fitnessLevelProfile.gym.copy(deadlift1RM = event.weight),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Running) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Running.DistanceIn30MinsChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                running = it.fitnessLevelProfile.running.copy(distanceIn30Mins = event.length),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Swimming) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Swimming.FreestyleDistance15MinChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                swimming = it.fitnessLevelProfile.swimming.copy(
                                    freestyleDistance15Min = event.length
                                ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesAdded ->
                trainingPreferencesStateFlow.update {
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

            is OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesRemoved ->
                trainingPreferencesStateFlow.update {
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

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.FreeBody) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.FreeBody.HasTrainedBeforeToggle ->
                trainingPreferencesStateFlow.update {
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

            is OnboardingEvent.FitnessLevelProfile.FreeBody.MaxPushupsChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics = it.fitnessLevelProfile.calisthenics.copy(
                                    maxPushups = event.maxPushups
                                ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.FreeBody.WallSitHoldChange ->
                trainingPreferencesStateFlow.update {
                    it.copy(
                        fitnessLevelProfile =
                            it.fitnessLevelProfile.copy(
                                calisthenics = it.fitnessLevelProfile.calisthenics.copy(
                                    wallSitHold = event.duration
                                ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.FreeBody.CanPlank30SecToggle ->
                trainingPreferencesStateFlow.update {
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

    private fun onBackClick() {
        when {
            _currentOnboardingStepStateFlow.value.ordinal != 0 ->
                navigate(_currentOnboardingStepStateFlow.value, -1)

            else -> logout()
        }
    }

    private fun onNextClick() {
        val currentStep = _currentOnboardingStepStateFlow.value
        if (!currentStep.canGoNext(trainingPreferencesStateFlow.value)) return
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
        _currentOnboardingStepStateFlow.value =
            OnboardingStep.entries[currentStep.ordinal + steps]
    }
}

fun OnboardingState.update(trainingPreferences: TrainingPreferences) =
    copy(
        trainingPreferences = trainingPreferences,
        canGoNext = currentStep.canGoNext(trainingPreferences)
    )

private fun OnboardingStep.canGoNext(data: TrainingPreferences) =
    when (this) {
        OnboardingStep.TrainingLevelSelection -> data.selectedTrainingLevel != null
        OnboardingStep.PhysicalProfile ->
            data.physicalProfile.weight != Weight.ZERO &&
                    data.physicalProfile.height != Length.ZERO

        OnboardingStep.ActivitiesSelection -> data.selectedActivities.isNotEmpty()
        OnboardingStep.FitnessLevelProfile -> validateCurrentFitnessLevel(data)
        OnboardingStep.ActivitiesRotationFrequency -> true
    }

private fun validateCurrentFitnessLevel(data: TrainingPreferences): Boolean {
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
