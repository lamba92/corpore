package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.WorkoutId
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: WorkoutId,
    val warmup: List<ExerciseWithMetrics>,
    val exercises: List<ExerciseWithMetrics>,
    val cooldown: List<ExerciseWithMetrics>,
)
