package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

sealed interface Id {
    val id: String
}

@Serializable
@JvmInline
value class ExerciseId(override val id: String) : Id

@Serializable
@JvmInline
value class MediaId(override val id: String) : Id

@Serializable
@JvmInline
value class DisplayableId(override val id: String) : Id

@Serializable
@JvmInline
value class WorkoutId(override val id: String) : Id