package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDisplayables(
    val id: DisplayableId,
    val exerciseId: ExerciseId,
    val name: String,
    val description: String,
    val instructions: List<String>,
    val locale: String,
)