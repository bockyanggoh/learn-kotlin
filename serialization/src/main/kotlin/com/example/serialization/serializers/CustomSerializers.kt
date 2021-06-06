package com.example.serialization.serializers

import com.example.serialization.utils.SystemConstants
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalAccessor
import java.util.*

object EpochDateSerializer: KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val epochValue = decoder.decodeLong()
        return LocalDateTime.ofEpochSecond(epochValue, 0, ZoneOffset.UTC)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val epochValue = value.toEpochSecond(ZoneOffset.UTC)
        encoder.encodeLong(epochValue)
    }
}

object UniversalLocalDateTimeSerializer: KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateValue = decoder.decodeString()
        return LocalDateTime.parse(dateValue, SystemConstants.dateFormat)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val dateValue = value.format(SystemConstants.dateFormat)
        encoder.encodeString(dateValue)
    }
}

object UUIDSerializer: KSerializer<UUID> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        val uuidValue = decoder.decodeString()
        return UUID.fromString(uuidValue)
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}