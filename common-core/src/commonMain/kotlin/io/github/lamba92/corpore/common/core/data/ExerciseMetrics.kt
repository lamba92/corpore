package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.Weight
import kotlinx.serialization.Serializable

@Serializable
sealed interface ExerciseMetrics {
    @Serializable
    data class Repetitions(
        val repetitions: Int,
    ) : ExerciseMetrics

    @Serializable
    data class SetsAndReps(
        val sets: List<Set>,
    ) : ExerciseMetrics {
        @Serializable
        data class Set(
            val weight: Weight,
            val reps: Int,
        )
    }

    @Serializable
    data class Duration(
        val duration: kotlin.time.Duration,
    ) : ExerciseMetrics

    @Serializable
    data class Distance(
        val distance: Length,
    ) : ExerciseMetrics
}
