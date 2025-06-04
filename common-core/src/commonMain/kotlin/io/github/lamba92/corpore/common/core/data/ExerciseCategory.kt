package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

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
