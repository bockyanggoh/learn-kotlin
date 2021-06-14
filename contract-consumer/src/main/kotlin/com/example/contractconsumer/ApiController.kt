package com.example.contractconsumer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/v1")
class ApiController(
    val restTemplate: RestTemplate,
    @Value("\${application.remote.createApi}") val createApiUrl: String,
    @Value("\${application.remote.getApi}") val getApiUrl: String

) {

    @GetMapping("/{id}")
    fun getApiResponse(@PathVariable id: String): ResponseEntity<ApiResponse> {
        val headers = HttpHeaders()
        val response = restTemplate.exchange(
            "${getApiUrl}/${id}",
            HttpMethod.GET,
            HttpEntity(null, headers),
            ApiResponse::class.java
        )
        return response
    }

    @PostMapping
    fun createApiResponse(@Valid @RequestBody request: ApiRequest): ResponseEntity<ApiResponse> {
        val headers = HttpHeaders()
        val response = restTemplate.exchange(
            createApiUrl,
            HttpMethod.POST,
            HttpEntity(request, headers),
            ApiResponse::class.java
        )
        return response
    }


}

@Serializable
data class ApiRequest(
    @field:NotNull
    @field:Size(min = 4, max = 50)
    val name: String?,
    @field:NotNull
    val value1: String?,
)

@Serializable
data class ApiResponse(
    val id: String,
    val name: String,
    val value1: String,
    val value2: String
) {

}

@Serializable
data class ErrorResponse(
    val id: String,
    @Serializable(with = DateTimeSerializer::class)
    val transactionTs: LocalDateTime = LocalDateTime.now(),
    val errorCode: String,
    val errorDescription: String
)

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [NoRecordFound::class])
    fun handleError(error: NoRecordFound): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                id = error.id,
                errorCode = error.errorCode!!,
                errorDescription = error.message!!
            )
        )
    }
}

class NoRecordFound(
    val id: String,
    val errorCode: String? = NoRecordFound::class.simpleName,
    message: String?
): Exception(message)

@Configuration
class AppConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate(
            listOf(
                KotlinSerializationJsonHttpMessageConverter()
            )
        )
    }
}

object DateTimeSerializer: KSerializer<LocalDateTime> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateValue = decoder.decodeString()
        return LocalDateTime.parse(dateValue, formatter)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val dateValue = value.format(formatter)
        encoder.encodeString(dateValue)
    }
}
