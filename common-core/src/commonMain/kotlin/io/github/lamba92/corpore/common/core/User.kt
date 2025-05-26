package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UserId,
    val email: String,
    val name: String,
    val pictureUrl: String? = null
)