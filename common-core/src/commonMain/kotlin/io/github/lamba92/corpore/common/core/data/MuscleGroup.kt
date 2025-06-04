package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

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
