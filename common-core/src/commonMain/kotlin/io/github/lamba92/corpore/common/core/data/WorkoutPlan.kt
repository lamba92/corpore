package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.UserId
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPlan(
    val userId: UserId,
    val workouts: List<Workout>,
    val currentWorkout: Int = 0,
    val currentWorkoutSession: WorkoutSession = WorkoutSession.NONE,
)
