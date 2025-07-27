@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

typealias EpochMillisSerializableInstant =
    @Serializable(with = EpochMillisInstantSerializer::class)
    Instant

object EpochMillisInstantSerializer : KSerializer<Instant> {
    override val descriptor =
        PrimitiveSerialDescriptor(
            serialName = Instant::class.qualifiedName!!,
            kind = PrimitiveKind.LONG,
        )

    override fun serialize(
        encoder: Encoder,
        value: Instant,
    ) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }

    override fun deserialize(decoder: Decoder) = Instant.Companion.fromEpochMilliseconds(decoder.decodeLong())
}
