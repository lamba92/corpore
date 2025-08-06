package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class UserWorkoutInfo(
    val currentWorkoutSession: WorkoutTimings,
    val createdAt: EpochMillisSerializableInstant,
)
