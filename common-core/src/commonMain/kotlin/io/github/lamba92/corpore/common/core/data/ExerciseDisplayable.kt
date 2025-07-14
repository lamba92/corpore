package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.DisplayableId
import io.github.lamba92.corpore.common.core.ExerciseId
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDisplayable(
    val id: DisplayableId,
    val exerciseId: ExerciseId,
    val name: String,
    val description: String,
    val instructions: List<String>,
    val locale: Locale,
)
