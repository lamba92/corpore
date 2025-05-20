package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val category: ExerciseCategory,
    val equipment: List<EquipmentType>,
    val primaryMuscles: List<MuscleGroup>,
    val secondaryMuscles: List<MuscleGroup> = emptyList(),
    val difficulty: DifficultyLevel,
    val instructions: List<String>,
    val media: List<MediaResource> = emptyList(),
    val metrics: ExerciseMetrics,
)

@Serializable
enum class ExerciseCategory {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    BALANCE,
    HIIT,
    SWIMMING,
    OTHER,
}

@Serializable
enum class EquipmentType {
    NONE,
    DUMBBELL,
    BARBELL,
    KETTLEBELL,
    MACHINE,
    RESISTANCE_BAND,
    BODYWEIGHT,
    OTHER,
}

@Serializable
enum class MuscleGroup {
    CHEST,
    BACK,
    LEGS,
    ARMS,
    SHOULDERS,
    CORE,
    FULL_BODY,
    OTHER,
}

@Serializable
enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
}

@Serializable
data class MediaResource(
    val type: MediaType,
    val url: String,
    val description: String? = null,
)

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO,
    GIF,
}

@Serializable
sealed interface ExerciseMetrics {
    @Serializable
    data class StrengthMetrics(
        val sets: List<Set>,
    ) : ExerciseMetrics {
        @Serializable
        data class Set(
            val weight: Weight,
            val reps: Int,
        )
    }

    @Serializable
    data class CardioMetrics(
        val durationMinutes: Int,
        val distance: Length? = null,
    ) : ExerciseMetrics

    @Serializable
    data class FlexibilityMetrics(
        val durationMinutes: Int,
        val holdTimeSeconds: Duration? = null,
    ) : ExerciseMetrics

    @Serializable
    data class HIITMetrics(
        val rounds: Int,
        val workDuration: Duration,
        val restDuration: Duration,
    ) : ExerciseMetrics

    @Serializable
    data class SwimmingMetrics(
        val distance: Length,
        val stroke: Stroke,
        val duration: Duration,
    ) : ExerciseMetrics {
        enum class Stroke {
            FREESTYLE,
            BUTTERFLY,
            BACKSTROKE,
            BREASTSTROKE,
        }
    }

    @Serializable
    data class OtherMetrics(
        val notes: String,
    ) : ExerciseMetrics
}
