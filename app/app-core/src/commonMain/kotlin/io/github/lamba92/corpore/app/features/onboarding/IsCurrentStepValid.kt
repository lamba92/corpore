package io.github.lamba92.corpore.app.features.onboarding

fun OnboardingState.isCurrentStepValid(): Boolean =
    when (currentStep) {
        OnboardingStepNames.TrainingLevelSelection -> stepsData.trainingLevelSelection.isValid
        OnboardingStepNames.PhysicalProfile -> stepsData.physicalProfile.isValid
        OnboardingStepNames.ActivitiesSelection -> stepsData.activitiesSelection.isValid
        OnboardingStepNames.FitnessLevelProfile -> stepsData.fitnessLevelProfile.isValid
        OnboardingStepNames.ActivitiesRotationFrequency -> stepsData.rotationFrequency.isValid
    }
