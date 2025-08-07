package io.github.lamba92.corpore.app.features.onboarding

import io.github.lamba92.corpore.app.features.onboarding.components.content.TrainingLevel
import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.Weight
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface OnboardingEvent {
    @Serializable
    data object NextClick : OnboardingEvent

    @Serializable
    data object BackClick : OnboardingEvent

    @Serializable
    data class TrainingLevelSelected(
        val trainingLevel: TrainingLevel,
    ) : OnboardingEvent

    @Serializable
    data class MeasurementSystemSelected(
        val measurementUnitSystem: MeasurementUnitSystem,
    ) : OnboardingEvent

    @Serializable
    sealed interface PhysicalProfile : OnboardingEvent {
        @Serializable
        data class YearOfBirthSelected(
            val year: Int,
        ) : PhysicalProfile

        @Serializable
        data class WeightSelected(
            val weight: Weight,
        ) : PhysicalProfile

        @Serializable
        data class HeightSelected(
            val height: Length,
        ) : PhysicalProfile
    }

    @Serializable
    sealed interface ActivitiesSelection : OnboardingEvent {
        @Serializable
        data class ActivityAdded(
            val activities: List<SportActivity>,
        ) : ActivitiesSelection

        @Serializable
        data class ActivityRemoved(
            val activities: List<SportActivity>,
        ) : ActivitiesSelection
    }

    @Serializable
    sealed interface FitnessLevelProfile : OnboardingEvent {
        @Serializable
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

        @Serializable
        sealed interface Running : FitnessLevelProfile {
            data class DistanceIn30MinsChange(
                val length: Length,
            ) : Running
        }

        @Serializable
        sealed interface Swimming : FitnessLevelProfile {
            data class FreestyleDistance15MinChange(
                val length: Length,
            ) : Swimming

            data class KnownStrokesAdded(
                val stroke: OnboardingState.FitnessLevelProfileStep.Swimming.Strokes,
            ) : Swimming

            data class KnownStrokesRemoved(
                val stroke: OnboardingState.FitnessLevelProfileStep.Swimming.Strokes,
            ) : Swimming
        }

        @Serializable
        sealed interface Calisthenics : FitnessLevelProfile {
            data object HasTrainedBeforeToggle : Calisthenics

            data class MaxPushupsChange(
                val maxPushups: Int,
            ) : Calisthenics

            data class WallSitHoldChange(
                val duration: Duration,
            ) : Calisthenics

            data object CanPlank30SecToggle : Calisthenics
        }
    }

    @Serializable
    data class ActivitiesRotationFrequencySelected(
        val frequency: OnboardingState.RotationFrequencyStep.RotationFrequency,
    ) : OnboardingEvent
}
