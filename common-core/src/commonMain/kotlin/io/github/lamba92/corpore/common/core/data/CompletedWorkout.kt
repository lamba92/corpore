package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class CompletedWorkout(
    val workout: Workout,
    val timings: WorkoutTimings,
    val feedback: WorkoutFeedback,
)
