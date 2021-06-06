package com.example.serialization

import kotlinx.serialization.Serializable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootApplication
class SerializationApplication

fun main(args: Array<String>) {
    runApplication<SerializationApplication>(*args)
}

@Configuration
class AppConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate(listOf(KotlinSerializationJsonHttpMessageConverter()))
    }
}

@Component
@RestController
class ApiController(
    private val service: SimpleService
) {

    @GetMapping("/simple/{id}")
    fun getSimpleApi(@PathVariable id: Int): ResponseEntity<SimpleResponse> {
        val response = service.getRecord(1)
        return ResponseEntity.ok(response)
    }
}

@Component
class SimpleService(
    private val restTemplate: RestTemplate
) {
    fun getRecord(id: Int): SimpleResponse {
        try {
            val response = restTemplate.getForEntity(
                "${Constants.baseUrl}/simple/$id",
                SimpleResponse::class.java
            )
            if (response.body != null)
                response.body
            throw Exception("lalala")
        } catch (e: Exception) {
            throw Exception("lalala")
        }
    }
}

@Serializable
data class SimpleRequest(
    val id: Int,
    val name: String,
    val dateTs: String? = LocalDateTime.now().format(Constants.dateFormat)
)

@Serializable
data class SimpleResponse(
    val id: Int,
    val name: String,
    val dateTs: String
)

object Constants {
        final val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        final val baseUrl: String = "http://localhost:3000"
}