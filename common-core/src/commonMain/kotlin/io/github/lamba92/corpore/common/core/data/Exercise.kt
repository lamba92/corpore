package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.ExerciseId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Exercise {
    val category: ExerciseCategory
    val primaryMuscles: List<MuscleGroup>
    val secondaryMuscles: List<MuscleGroup>
    val isVerified: Boolean

    @Serializable
    data class WithId(
        val id: ExerciseId,
        override val category: ExerciseCategory,
        override val primaryMuscles: List<MuscleGroup>,
        override val secondaryMuscles: List<MuscleGroup>,
        override val isVerified: Boolean,
    ) : Exercise

    @Serializable
    data class WithoutId(
        override val category: ExerciseCategory,
        override val primaryMuscles: List<MuscleGroup>,
        override val secondaryMuscles: List<MuscleGroup>,
        override val isVerified: Boolean,
    ) : Exercise
}
