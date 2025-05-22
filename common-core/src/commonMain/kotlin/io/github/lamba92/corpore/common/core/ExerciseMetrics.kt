package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ExerciseWithMetrics(
    val exercise: Exercise.WithId,
    val metrics: ExerciseMetrics,
)

@Serializable
sealed interface ExerciseMetrics {
    @Serializable
    data class StrengthMetrics(
        val sets: List<Set>,
    ) : ExerciseMetrics {
        @Serializable
        data class Set(
            val weight: Weight,
            val reps: Int,
        )
    }

    @Serializable
    data class CardioMetrics(
        val durationMinutes: Int,
        val distance: Length? = null,
    ) : ExerciseMetrics

    @Serializable
    sealed interface HIITMetrics : ExerciseMetrics {
        val rounds: Int
        val restDuration: Duration

        @Serializable
        data class WithRepetitions(
            override val rounds: Int,
            override val restDuration: Duration,
            val repetitions: Int,
        ) : HIITMetrics

        @Serializable
        data class WithDuration(
            override val rounds: Int,
            override val restDuration: Duration,
            val duration: Duration,
        ) : HIITMetrics
    }

    @Serializable
    data class SwimmingMetrics(
        val distance: Length,
    ) : ExerciseMetrics

    @Serializable
    data class OtherMetrics(
        val notes: String,
    ) : ExerciseMetrics
}

@Serializable
data class DailyWorkoutPlan(
    val exercises: List<ExerciseWithMetrics>,
)
