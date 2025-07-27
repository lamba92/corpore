@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.UserId
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class User(
    val id: UserId,
    val email: String,
    val name: String,
    val pictureUrl: String? = null,
)
