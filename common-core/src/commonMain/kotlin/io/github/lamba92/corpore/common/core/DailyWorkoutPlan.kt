package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
data class DailyWorkoutPlan(
    val id: DailyWorkoutPlanId,
    val warmup: List<ExerciseWithMetrics>,
    val exercises: List<ExerciseWithMetrics>,
    val cooldown: List<ExerciseWithMetrics> = emptyList()
)