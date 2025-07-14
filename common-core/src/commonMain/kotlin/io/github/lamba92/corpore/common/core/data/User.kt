package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.UserId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UserId,
    val email: String,
    val name: String,
    val pictureUrl: String? = null,
    val workoutInfo: UserWorkoutInfo? = null,
)

@Serializable
data class UserWorkoutInfo(
    val currentWorkoutSession: WorkoutSession,
    val createdAt: Instant,
)
