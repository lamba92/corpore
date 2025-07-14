package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.WorkoutFeedbackId
import io.github.lamba92.corpore.common.core.WorkoutId
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutFeedback(
    val workoutId: WorkoutId,
    val feedbackId: WorkoutFeedbackId,
    val difficulty: Difficulty,
    val quality: Quality,
    val notes: String?,
) {
    enum class Difficulty {
        TOO_EASY,
        EASY,
        MEDIUM,
        HARD,
        TOO_HARD,
    }

    enum class Quality {
        TERRIBLE,
        BAD,
        OK,
        GOOD,
        EXCELLENT,
    }
}
