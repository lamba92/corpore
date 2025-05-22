package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
sealed interface Exercise {
    val category: ExerciseCategory
    val equipment: List<EquipmentType>
    val primaryMuscles: List<MuscleGroup>
    val secondaryMuscles: List<MuscleGroup>
    val difficulty: DifficultyLevel
    val isVerified: Boolean

    @Serializable
    data class WithId(
        val id: ExerciseId,
        override val category: ExerciseCategory,
        override val equipment: List<EquipmentType>,
        override val primaryMuscles: List<MuscleGroup>,
        override val secondaryMuscles: List<MuscleGroup>,
        override val difficulty: DifficultyLevel,
        override val isVerified: Boolean,
    ) : Exercise

    @Serializable
    data class WithoutId(
        override val category: ExerciseCategory,
        override val equipment: List<EquipmentType>,
        override val primaryMuscles: List<MuscleGroup>,
        override val secondaryMuscles: List<MuscleGroup>,
        override val difficulty: DifficultyLevel,
        override val isVerified: Boolean,
    ) : Exercise
}