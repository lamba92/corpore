package io.github.lamba92.corpore.app.features.onboarding

import io.github.lamba92.corpore.common.core.data.CalisthenicsFitness
import io.github.lamba92.corpore.common.core.data.GymFitness
import io.github.lamba92.corpore.common.core.data.MeasurementUnitSystem
import io.github.lamba92.corpore.common.core.data.RunningFitness
import io.github.lamba92.corpore.common.core.data.SportActivity
import io.github.lamba92.corpore.common.core.data.SwimmingFitness
import io.github.lamba92.corpore.common.core.data.TrainingFrequency
import io.github.lamba92.corpore.common.core.data.TrainingLevel
import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.Weight
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class OnboardingState(
    val measurementUnitSystem: MeasurementUnitSystem = MeasurementUnitSystem.Metric,
    val currentStep: OnboardingStepNames = OnboardingStepNames.TrainingLevelSelection,
    val isLoggingOut: Boolean = false,
    val stepsData: StepsData = StepsData(),
) {
    @Serializable
    data class StepsData(
        val trainingLevelSelection: TrainingLevelSelectionStep = TrainingLevelSelectionStep(),
        val physicalProfile: PhysicalProfileStep = PhysicalProfileStep(),
        val activitiesSelection: ActivitiesSelectionStep = ActivitiesSelectionStep(),
        val fitnessLevelProfile: FitnessLevelProfileStep = FitnessLevelProfileStep(),
        val rotationFrequency: TrainingFrequencyStep = TrainingFrequencyStep(),
    )

    companion object {
        val INITIAL = OnboardingState()
    }

    interface OnboardingStep {
        val isValid: Boolean
    }

    @Serializable
    data class TrainingLevelSelectionStep(
        val level: TrainingLevel? = null,
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = level != null
    }

    @Serializable
    data class PhysicalProfileStep(
        val yearOfBirth: Int? = null,
        val weight: Weight = Weight.ZERO,
        val height: Length = Length.ZERO,
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = yearOfBirth != null && weight != Weight.ZERO && height != Length.ZERO
    }

    @Serializable
    data class ActivitiesSelectionStep(
        val activities: Set<SportActivity> = emptySet(),
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = activities.isNotEmpty()
    }

    @Serializable
    data class FitnessLevelProfileStep(
        val gym: GymFitness = GymFitness(),
        val running: RunningFitness = RunningFitness(),
        val swimming: SwimmingFitness = SwimmingFitness(),
        val calisthenics: CalisthenicsFitness = CalisthenicsFitness(),
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = gym.isValid && running.isValid && swimming.isValid && calisthenics.isValid

    }

    @Serializable
    data class TrainingFrequencyStep(
        val frequency: TrainingFrequency? = null,
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = frequency != null
    }
}

val CalisthenicsFitness.isValid: Boolean
    get() =
        when {
            hasTrainedBefore -> maxPushups != 0 && wallSitHold != Duration.ZERO
            else -> true
        }

val SwimmingFitness.isValid: Boolean
    get() =
        when {
            hasTrainedBefore -> freestyleDistance15Min != Length.ZERO
            else -> true
        }

val RunningFitness.isValid: Boolean
    get() =
        when {
            hasTrainedBefore -> distanceIn30Mins != Length.ZERO
            else -> true
        }

val GymFitness.isValid: Boolean
    get() =
        when {
            hasTrainedBefore ->
                benchPress1RM != Weight.ZERO &&
                    squat1RM != Weight.ZERO &&
                    deadlift1RM != Weight.ZERO
            else -> true
        }
