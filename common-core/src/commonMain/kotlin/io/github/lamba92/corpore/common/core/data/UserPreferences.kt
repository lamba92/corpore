package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.Weight
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val trainingLevel: TrainingLevel,
    val yearOfBirth: Int,
    val weight: Weight,
    val height: Length,
    val
)