package com.example.serialization

import com.example.serialization.models.ErrorResponse
import com.example.serialization.models.SimpleRequest
import com.example.serialization.models.SimpleResponse
import com.example.serialization.serializers.EpochDateSerializer
import com.example.serialization.serializers.UniversalLocalDateTimeSerializer
import com.example.serialization.services.SimpleService
import com.example.serialization.utils.SystemConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootApplication
class SerializationApplication

fun main(args: Array<String>) {
    runApplication<SerializationApplication>(*args)
}


@Component
@RestController
class ApiController(
    private val service: SimpleService
) {
    companion object {
        val log = KotlinLogging.logger {}
    }

    @GetMapping("/simple/{id}")
    fun getSimpleApi(@PathVariable id: Int, @RequestParam type: String): ResponseEntity<SimpleResponse> {
        if (type.equals("epoch", ignoreCase = true)) {
            val response = service.getEpochRecord(id)
            return ResponseEntity.ok(response)
        }
        else if(type.equals("date", ignoreCase = true)) {
            val response = service.getRecord(id)
            return ResponseEntity.ok(response)
        }
        throw InvalidInputException(message = "Invalid parameter [type] given. Please only use the following values [epoch|date]")
    }

    @PostMapping("/simple")
    fun createRecord(@RequestBody @Validated req:SimpleRequest, @RequestParam type: String): ResponseEntity<SimpleResponse> {
        if (type.equals("epoch", ignoreCase = true)) {
            val response = service.createEpochRecord(req)
            return ResponseEntity.ok(response)
        }
        else if(type.equals("date", ignoreCase = true)) {
            val response = service.createRecord(req)
            return ResponseEntity.ok(response)
        }

        throw InvalidInputException(message = "Invalid parameter [type] given. Please only use the following values [epoch|date]")
    }
}

@RestControllerAdvice
class CommonExceptionHandler() {
    @ExceptionHandler(value = [
        InvalidInputException::class,
        InvalidIdException::class
    ])
    fun invalidInputResponse(exception: Exception): ErrorResponse {
        return ErrorResponse(message = exception.message)
    }
}

class InvalidInputException(
    message: String
): Exception(message) {
}

class InvalidIdException(
    message: String
): Exception(message) {

}