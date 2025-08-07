@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.app.features.onboarding

import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthService
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.app.core.viewmodel.MVIViewModel
import io.github.lamba92.corpore.common.core.usecase.execute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class OnboardingViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val authService: AuthService,
) : MVIViewModel<OnboardingState, OnboardingEffects, OnboardingEvent>() {
    private val _state = MutableStateFlow(OnboardingState.INITIAL)
    override val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingEffects>()
    override val effects: SharedFlow<OnboardingEffects> = _effects.asSharedFlow()

    private fun logout() {
        _state.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch { logoutUseCase.execute() }
        authService
            .authSession
            .filter { it == null }
            .take(1)
            .onEach { _effects.emit(OnboardingEffects.Logout) }
            .onEach { _state.update { it.copy(isLoggingOut = false) } }
            .launchIn(viewModelScope)
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
                        stepsData =
                            it.stepsData.copy(
                                rotationFrequency = OnboardingState.RotationFrequencyStep(event.frequency),
                            ),
                    )
                }

            OnboardingEvent.BackClick -> onBackClick()
            OnboardingEvent.NextClick -> onNextClick()
        }
    }

    private fun onUpdate(event: OnboardingEvent.MeasurementSystemSelected) {
        _state.update { it.copy(measurementUnitSystem = event.measurementUnitSystem) }
    }

    private fun onUpdate(event: OnboardingEvent.TrainingLevelSelected) {
        _state.update {
            it.copy(
                stepsData =
                    it.stepsData.copy(
                        trainingLevelSelection =
                            it.stepsData.trainingLevelSelection.copy(
                                level = event.trainingLevel,
                            ),
                    ),
            )
        }
    }

    private fun onEvent(event: OnboardingEvent.PhysicalProfile) {
        when (event) {
            is OnboardingEvent.PhysicalProfile.HeightSelected ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                physicalProfile = it.stepsData.physicalProfile.copy(height = event.height),
                            ),
                    )
                }

            is OnboardingEvent.PhysicalProfile.WeightSelected ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                physicalProfile = it.stepsData.physicalProfile.copy(weight = event.weight),
                            ),
                    )
                }

            is OnboardingEvent.PhysicalProfile.YearOfBirthSelected ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                physicalProfile = it.stepsData.physicalProfile.copy(yearOfBirth = event.year),
                            ),
                    )
                }
        }
    }

    fun onEvent(event: OnboardingEvent.ActivitiesSelection) {
        when (event) {
            is OnboardingEvent.ActivitiesSelection.ActivityAdded ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                activitiesSelection =
                                    it.stepsData.activitiesSelection.copy(
                                        activities = it.stepsData.activitiesSelection.activities + event.activities.toSet(),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.ActivitiesSelection.ActivityRemoved ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                activitiesSelection =
                                    it.stepsData.activitiesSelection.copy(
                                        activities = it.stepsData.activitiesSelection.activities - event.activities.toSet(),
                                    ),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Gym -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.Running -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.Swimming -> onEvent(event)
            is OnboardingEvent.FitnessLevelProfile.Calisthenics -> onEvent(event)
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Gym) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Gym.BenchPress1RMChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        gym =
                                            it.stepsData.fitnessLevelProfile.gym
                                                .copy(benchPress1RM = event.weight),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Gym.Squat1RMChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        gym =
                                            it.stepsData.fitnessLevelProfile.gym
                                                .copy(squat1RM = event.weight),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Gym.Deadlift1RMChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        gym =
                                            it.stepsData.fitnessLevelProfile.gym
                                                .copy(deadlift1RM = event.weight),
                                    ),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Running) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Running.DistanceIn30MinsChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        running =
                                            it.stepsData.fitnessLevelProfile.running.copy(
                                                distanceIn30Mins = event.length,
                                            ),
                                    ),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Swimming) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Swimming.FreestyleDistance15MinChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        swimming =
                                            it.stepsData.fitnessLevelProfile.swimming.copy(
                                                freestyleDistance15Min = event.length,
                                            ),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesAdded ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        swimming =
                                            it.stepsData.fitnessLevelProfile.swimming.copy(
                                                knownStrokes = it.stepsData.fitnessLevelProfile.swimming.knownStrokes + event.stroke,
                                            ),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesRemoved ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        swimming =
                                            it.stepsData.fitnessLevelProfile.swimming.copy(
                                                knownStrokes = it.stepsData.fitnessLevelProfile.swimming.knownStrokes - event.stroke,
                                            ),
                                    ),
                            ),
                    )
                }
        }
    }

    private fun onEvent(event: OnboardingEvent.FitnessLevelProfile.Calisthenics) {
        when (event) {
            is OnboardingEvent.FitnessLevelProfile.Calisthenics.HasTrainedBeforeToggle ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        calisthenics =
                                            it.stepsData.fitnessLevelProfile.calisthenics.copy(
                                                hasTrainedBefore = !it.stepsData.fitnessLevelProfile.calisthenics.hasTrainedBefore,
                                            ),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Calisthenics.MaxPushupsChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        calisthenics =
                                            it.stepsData.fitnessLevelProfile.calisthenics.copy(
                                                maxPushups = event.maxPushups,
                                            ),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Calisthenics.WallSitHoldChange ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        calisthenics =
                                            it.stepsData.fitnessLevelProfile.calisthenics.copy(
                                                wallSitHold = event.duration,
                                            ),
                                    ),
                            ),
                    )
                }

            is OnboardingEvent.FitnessLevelProfile.Calisthenics.CanPlank30SecToggle ->
                _state.update {
                    it.copy(
                        stepsData =
                            it.stepsData.copy(
                                fitnessLevelProfile =
                                    it.stepsData.fitnessLevelProfile.copy(
                                        calisthenics =
                                            it.stepsData.fitnessLevelProfile.calisthenics.copy(
                                                canPlank30Sec = !it.stepsData.fitnessLevelProfile.calisthenics.canPlank30Sec,
                                            ),
                                    ),
                            ),
                    )
                }
        }
    }

    internal fun onBackClick() {
        when {
            _state.value.currentStep.ordinal != 0 ->
                navigate(_state.value.currentStep, -1)

            else -> logout()
        }
    }

    private fun onNextClick() {
        val currentState = _state.value
        val isStepValid = currentState.isCurrentStepValid()

        if (!isStepValid) return
        when {
            currentState.currentStep.ordinal == OnboardingStepNames.entries.lastIndex ->
                _effects.tryEmit(OnboardingEffects.OnboardingComplete)

            else -> navigate(currentState.currentStep, 1)
        }
    }

    private fun navigate(
        currentStep: OnboardingStepNames,
        steps: Int,
    ) {
        _state.update {
            it.copy(currentStep = OnboardingStepNames.entries[currentStep.ordinal + steps])
        }
    }
}
