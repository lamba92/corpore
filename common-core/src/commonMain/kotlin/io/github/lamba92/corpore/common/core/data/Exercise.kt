package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val category: ExerciseCategory,
    val primaryMuscles: List<MuscleGroup>,
    val isVerified: Boolean,
    val displayables: Map<Locale, ExerciseDisplayable>,
    val medias: List<MediaResourceMetadata>,
)
