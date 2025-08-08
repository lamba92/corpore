package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWorkout(
    val workout: Workout,
    val timings: WorkoutTimings,
)
