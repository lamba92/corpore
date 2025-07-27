package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.UserId
import kotlinx.serialization.Serializable

interface TokenRepository {
    suspend fun createToken(id: UserId): CreateTokenResult

    suspend fun revokeToken(token: String): Boolean

    suspend fun revokeAllTokens(userId: String): Boolean

    @Serializable
    sealed interface CreateTokenResult {
        @Serializable
        data class Success(
            val accessToken: SerializableJWT,
            val refreshToken: SerializableJWT,
        ) : CreateTokenResult

        @JvmInline
        @Serializable
        value class Failure(
            val reason: String,
        ) : CreateTokenResult
    }
}
