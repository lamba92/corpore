package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDisplayable(
    val name: String,
    val description: String,
    val instructions: List<String>,
    val locale: Locale,
)
