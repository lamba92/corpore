package io.github.lamba92.corpore.common.core.http.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val oauthToken: String,
)
