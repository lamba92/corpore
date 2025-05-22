package io.github.lamba92.corpore.common.core

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