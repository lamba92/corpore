package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.ui.onboarding.TrainingLevel
import io.github.lamba92.corpore.app.core.usecase.execute
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.LengthUnit
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.utils.WeightUnit
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

@Serializable
data class OnboardingData(
    val selectedTrainingLevel: TrainingLevel? = null,
    val physicalProfile: PhysicalProfile = PhysicalProfile(),
    val selectedActivities: List<SportActivity> = emptyList(),
) {
    @Serializable
    data class PhysicalProfile(
        val yearOfBirth: Int? = null,
        val weight: Weight? = null,
        val height: Length? = null,
        val measurementSystem: MeasurementSystem = MeasurementSystem.Metric,
    )
}

enum class MeasurementSystem(
    val weightUnit: WeightUnit,
    val lengthUnit: LengthUnit,
) {
    Metric(
        weightUnit = WeightUnit.Kilograms,
        lengthUnit = LengthUnit.Centimeters,
    ),
    Imperial(
        weightUnit = WeightUnit.Pounds,
        lengthUnit = LengthUnit.Feet,
    ),
}

enum class SportActivity {
    Gym,
    Running,
    Swimming,
    FreeBody,
}

sealed interface OnboardingDataUpdateEvent {
    data class TrainingLevelSelected(val trainingLevel: TrainingLevel) : OnboardingDataUpdateEvent

    sealed interface PhysicalProfile : OnboardingDataUpdateEvent {
        data class YearOfBirthSelected(val year: Int) : PhysicalProfile

        data class WeightSelected(val weight: Weight) : PhysicalProfile

        data class HeightSelected(val height: Length) : PhysicalProfile

        data class MeasurementSystemSelected(val measurementSystem: MeasurementSystem) :
            PhysicalProfile
    }

    sealed interface ActivitiesSelection : OnboardingDataUpdateEvent {
        data class ActivityAdded(val activities: List<SportActivity>) : ActivitiesSelection

        data class ActivityRemoved(val activities: List<SportActivity>) : ActivitiesSelection
    }
}

enum class OnboardingStep {
    TrainingLevelSelection,
    PhysicalProfile,
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
                    onboardingDataStateFlow.value.copy(selectedTrainingLevel = event.trainingLevel)

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

            is OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow
                        .value
                        .copy(
                            physicalProfile =
                                onboardingDataStateFlow
                                    .value
                                    .physicalProfile
                                    .copy(yearOfBirth = event.year),
                        )

            is OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow
                        .value
                        .copy(
                            physicalProfile =
                                onboardingDataStateFlow
                                    .value
                                    .physicalProfile
                                    .copy(weight = event.weight),
                        )

            is OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow
                        .value
                        .copy(
                            physicalProfile =
                                onboardingDataStateFlow
                                    .value
                                    .physicalProfile
                                    .copy(height = event.height),
                        )

            is OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected ->
                onboardingDataStateFlow.value =
                    onboardingDataStateFlow
                        .value
                        .copy(
                            physicalProfile =
                                onboardingDataStateFlow
                                    .value
                                    .physicalProfile
                                    .copy(measurementSystem = event.measurementSystem),
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
                data.physicalProfile.weight != null &&
                data.physicalProfile.height != null

        OnboardingStep.ActivitiesSelection -> data.selectedActivities.isNotEmpty()
        OnboardingStep.CurrentFitnessLevelUserInput -> true
        OnboardingStep.ActivitiesRotationFrequency -> true
    }
