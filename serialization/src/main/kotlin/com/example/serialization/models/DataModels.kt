package com.example.serialization.models

import com.example.serialization.serializers.EpochDateSerializer
import com.example.serialization.serializers.UUIDSerializer
import com.example.serialization.serializers.UniversalLocalDateTimeSerializer
import com.example.serialization.utils.SystemConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class SimpleRequest(
    val id: Int,
    val name: String,
    val dateTs: String? = LocalDateTime.now().format(SystemConstants.dateFormat)
)

@Serializable
data class SimpleResponse(
    val id: Int,
    val name: String,
    @Serializable(with = UniversalLocalDateTimeSerializer::class)
    val dateTs: LocalDateTime
)

@Serializable
data class EpochResponse(
    val id: Int,
    val name: String,
    @Serializable(with = EpochDateSerializer::class)
    @SerialName("dateEpoch")
    val dateTs: LocalDateTime
)

@Serializable
data class ErrorResponse(
    @Serializable(with = UUIDSerializer::class)
    val eventId: UUID? = UUID.randomUUID(),
    @Serializable(with = UniversalLocalDateTimeSerializer::class)
    val transactionTs: LocalDateTime? = LocalDateTime.now(),
    val message: String?
)