package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.WorkoutId
import io.github.lamba92.corpore.common.core.WorkoutSessionId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class WorkoutSession(
    val workoutSessionId: WorkoutSessionId,
    val workoutId: WorkoutId,
    val timings: Timings,
    val startTime: Instant,
    val endTime: Instant,
) {
    @Serializable
    data class Timings(
        val warmup: List<Duration>,
        val exercises: List<Duration>,
        val cooldown: List<Duration>,
    )
}
