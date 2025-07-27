package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ExerciseId(
    val value: String,
)

@Serializable
@JvmInline
value class MediaId(
    val value: String,
)

@Serializable
@JvmInline
value class DisplayableId(
    val value: String,
)

@Serializable
@JvmInline
value class WorkoutId(
    val value: String,
)

@Serializable
@JvmInline
value class WorkoutSessionId(
    val value: String,
)

@Serializable
@JvmInline
value class WorkoutPlanId(
    val value: String,
)

@Serializable
@JvmInline
value class UserId(
    val email: String,
)

@Serializable
@JvmInline
value class WorkoutFeedbackId(
    val value: String,
)
