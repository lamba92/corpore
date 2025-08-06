package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutFeedback(
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
