package io.github.lamba92.corpore.server.data

import com.appstractive.jwt.JWT
import com.appstractive.jwt.from
import io.github.lamba92.corpore.common.core.UserId
import io.github.lamba92.corpore.common.core.data.User
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface UserRepository {
    suspend fun getOrCreateUser(
        email: String,
        name: String,
        pictureUrl: String? = null,
    ): User
}

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
        value class Failure(val reason: String) : CreateTokenResult
    }
}

typealias SerializableJWT =
    @Serializable(with = JWTSerializer::class)
    JWT

object JWTSerializer : KSerializer<JWT> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(JWT::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): JWT = JWT.from(decoder.decodeString())

    override fun serialize(
        encoder: Encoder,
        value: JWT,
    ) {
        encoder.encodeString(value.toString())
    }
}
