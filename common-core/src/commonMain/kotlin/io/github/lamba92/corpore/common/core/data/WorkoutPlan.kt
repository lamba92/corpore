package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPlan(
    val workouts: List<Workout>,
    val currentWorkout: Int = 0,
    val currentWorkoutTimings: WorkoutTimings = WorkoutTimings.NONE,
)
