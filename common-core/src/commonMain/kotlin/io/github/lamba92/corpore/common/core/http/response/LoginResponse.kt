@file:Suppress("FunctionName")

package io.github.lamba92.corpore.common.core.http.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
)

fun ApiResponse(accessToken: String) =
    ApiResponse(
        data =
            RefreshTokenResponse(
                accessToken = accessToken,
            ),
    )

fun ApiResponse(
    accessToken: String,
    refreshToken: String,
) = ApiResponse(
    data =
        LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        ),
)
