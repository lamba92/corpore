package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

sealed interface Id {
    val value: String
}

@Serializable
@JvmInline
value class ExerciseId(override val value: String) : Id

@Serializable
@JvmInline
value class MediaId(override val value: String) : Id

@Serializable
@JvmInline
value class DisplayableId(override val value: String) : Id

@Serializable
@JvmInline
value class WorkoutId(override val value: String) : Id

@Serializable
@JvmInline
value class WorkoutSessionId(override val value: String) : Id

@Serializable
@JvmInline
value class WeeklyPlanId(override val value: String) : Id

@Serializable
@JvmInline
value class UserId(override val value: String) : Id

@Serializable
@JvmInline
value class WorkoutFeedbackId(override val value: String) : Id
