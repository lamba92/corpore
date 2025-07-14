package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.WeeklyPlanId
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyPlan(
    val weeklyPlanId: WeeklyPlanId,
    val workouts: List<Workout>,
)
