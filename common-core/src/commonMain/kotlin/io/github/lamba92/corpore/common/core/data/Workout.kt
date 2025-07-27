package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val warmup: List<ExerciseWithMetrics>,
    val exercises: List<ExerciseWithMetrics>,
    val cooldown: List<ExerciseWithMetrics>,
)
