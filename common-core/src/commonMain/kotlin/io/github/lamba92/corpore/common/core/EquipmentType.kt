package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

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