package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.ExerciseId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Exercise {
    val category: ExerciseCategory
    val primaryMuscles: List<MuscleGroup>
    val isVerified: Boolean

    val displayables: Map<Locale, ExerciseDisplayable>

    @Serializable
    data class WithId(
        val id: ExerciseId,
        override val category: ExerciseCategory,
        override val primaryMuscles: List<MuscleGroup>,
        override val isVerified: Boolean,
        override val displayables: Map<Locale, ExerciseDisplayable>,
    ) : Exercise

    @Serializable
    data class WithoutId(
        override val category: ExerciseCategory,
        override val primaryMuscles: List<MuscleGroup>,
        override val isVerified: Boolean,
        override val displayables: Map<Locale, ExerciseDisplayable>,
    ) : Exercise
}
