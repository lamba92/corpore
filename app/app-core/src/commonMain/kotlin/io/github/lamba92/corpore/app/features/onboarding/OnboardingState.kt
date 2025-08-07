package io.github.lamba92.corpore.app.features.onboarding

import io.github.lamba92.corpore.app.features.onboarding.components.content.TrainingLevel
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
        val rotationFrequency: RotationFrequencyStep = RotationFrequencyStep(),
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
        val weight: Weight = Weight.Companion.ZERO,
        val height: Length = Length.Companion.ZERO,
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = yearOfBirth != null && weight != Weight.Companion.ZERO && height != Length.Companion.ZERO
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
        val gym: Gym = Gym(),
        val running: Running = Running(),
        val swimming: Swimming = Swimming(),
        val calisthenics: Calisthenics = Calisthenics(),
    ) : OnboardingStep {
        override val isValid: Boolean
            get() = gym.isValid && running.isValid && swimming.isValid && calisthenics.isValid

        @Serializable
        data class Gym(
            val hasTrainedBefore: Boolean = false,
            val benchPress1RM: Weight = Weight.Companion.ZERO,
            val squat1RM: Weight = Weight.Companion.ZERO,
            val deadlift1RM: Weight = Weight.Companion.ZERO,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() =
                    when {
                        hasTrainedBefore ->
                            benchPress1RM != Weight.Companion.ZERO &&
                                squat1RM != Weight.Companion.ZERO &&
                                deadlift1RM != Weight.Companion.ZERO
                        else -> true
                    }
        }

        @Serializable
        data class Running(
            val hasTrainedBefore: Boolean = false,
            val distanceIn30Mins: Length = Length.Companion.ZERO,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() =
                    when {
                        hasTrainedBefore -> distanceIn30Mins != Length.Companion.ZERO
                        else -> true
                    }
        }

        @Serializable
        data class Swimming(
            val hasTrainedBefore: Boolean = false,
            val freestyleDistance15Min: Length = Length.Companion.ZERO,
            val knownStrokes: Set<Strokes> = setOf(Strokes.Freestyle),
        ) : OnboardingStep {
            enum class Strokes {
                Freestyle,
                Backstroke,
                Breaststroke,
                Butterfly,
            }

            override val isValid: Boolean
                get() =
                    when {
                        hasTrainedBefore -> freestyleDistance15Min != Length.Companion.ZERO
                        else -> true
                    }
        }

        @Serializable
        data class Calisthenics(
            val hasTrainedBefore: Boolean = false,
            val maxPushups: Int = 0,
            val wallSitHold: Duration = Duration.Companion.ZERO,
            val canPlank30Sec: Boolean = false,
        ) : OnboardingStep {
            override val isValid: Boolean
                get() =
                    when {
                        hasTrainedBefore -> maxPushups != 0 && wallSitHold != Duration.Companion.ZERO
                        else -> true
                    }
        }
    }

    @Serializable
    data class RotationFrequencyStep(
        val frequency: RotationFrequency? = null,
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
