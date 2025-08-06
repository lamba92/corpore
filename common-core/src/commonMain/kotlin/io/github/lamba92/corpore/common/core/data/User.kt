@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class User(
    val email: String,
    val name: String,
    val registeredAt: EpochMillisSerializableInstant,
    val pictureUrl: String? = null,
    val trainingPreferences: TrainingPreferences? = null,
)

@Serializable
data class TrainingPreferences(
    val
)