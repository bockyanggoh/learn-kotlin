package com.example.contractconsumer

import au.com.dius.pact.core.model.annotations.Pact
import kotlinx.serialization.Serializable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.collections.HashMap

@RestController
@RequestMapping("/api/v1")
class ApiController {
    companion object {
        val data: MutableMap<String, ApiResponse> = HashMap()
    }

    @GetMapping("/{id}")
    fun getApiResponse(@PathVariable id: String): ResponseEntity<ApiResponse> {
        val retrieved = data.get(id)
        if (retrieved != null) {
            return ResponseEntity.ok(retrieved)
        }
        throw NoRecordFound("Requested record [${id}] not found")
    }

    @PostMapping
    fun createApiResponse(@Valid @RequestBody request: ApiRequest): ResponseEntity<ApiResponse> {
        val generatedId = UUID.randomUUID().toString()
        val record = ApiResponse(
            id = generatedId,
            name = request.name!!,
            value1 = request.value1!!,
            value2 = request.name + request.value1
        )
        data[generatedId] = record
        return ResponseEntity.ok(record)
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
)


class NoRecordFound(
    message: String?
): Exception(message)