package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.ui.onboarding.TrainingLevel
import io.github.lamba92.corpore.app.core.usecase.execute
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OnboardingData(
    val selectedTrainingLevel: TrainingLevel? = null,
    val selectedActivities: List<SportActivity> = emptyList(),
)

enum class SportActivity {
    Gym,
    Running,
    Swimming,
    FreeBody,
}

sealed interface OnboardingDataUpdateEvent {
    data class TrainingLevelSelected(val trainingLevel: TrainingLevel) : OnboardingDataUpdateEvent

    data class ActivityAdded(val activity: SportActivity) : OnboardingDataUpdateEvent

    data class ActivityRemoved(val activity: SportActivity) : OnboardingDataUpdateEvent
}

enum class OnboardingStep {
    TrainingLevelSelection,
    UserData,
    ActivitiesSelection,
    CurrentFitnessLevelUserInput,
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
            when (currentOnboardingStep) {
                OnboardingStep.TrainingLevelSelection -> onboardingData.selectedTrainingLevel != null
                OnboardingStep.UserData -> true // TODO: implement
                OnboardingStep.ActivitiesSelection -> onboardingData.selectedActivities.isNotEmpty()
                OnboardingStep.CurrentFitnessLevelUserInput -> true // TODO: implement
                OnboardingStep.ActivitiesRotationFrequency -> true // TODO: implement
            }
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
                    onboardingDataStateFlow.value.copy(selectedTrainingLevel = event.trainingLevel)

            is OnboardingDataUpdateEvent.ActivityAdded ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        selectedActivities = onboardingDataStateFlow.value.selectedActivities + event.activity,
                    )

            is OnboardingDataUpdateEvent.ActivityRemoved ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow.value.copy(
                        selectedActivities = onboardingDataStateFlow.value.selectedActivities - event.activity,
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
        OnboardingStep.UserData -> true
        OnboardingStep.ActivitiesSelection -> data.selectedActivities.isNotEmpty()
        OnboardingStep.CurrentFitnessLevelUserInput -> true
        OnboardingStep.ActivitiesRotationFrequency -> true
    }
