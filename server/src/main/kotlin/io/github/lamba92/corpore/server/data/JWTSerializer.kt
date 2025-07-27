package io.github.lamba92.corpore.server.data

import com.appstractive.jwt.JWT
import com.appstractive.jwt.from
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias SerializableJWT =
    @Serializable(with = JWTSerializer::class)
    JWT

object JWTSerializer : KSerializer<JWT> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(JWT::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): JWT = JWT.Companion.from(decoder.decodeString())

    override fun serialize(
        encoder: Encoder,
        value: JWT,
    ) {
        encoder.encodeString(value.toString())
    }
}
