package com.example.java_kotlin

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class SimpleController {
    @GetMapping
    fun getApi(): ResponseEntity<String> {
        return ResponseEntity.ok("Success!")
    }

    @PostMapping
    fun postApi(@RequestBody x: SimpleEasyData): ResponseEntity<SimpleEasyData> {
        return ResponseEntity.ok(x)

    }
}

data class SimpleEasyData(
    @JsonProperty("requestTs") var date: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("name") var complexName: String? = "test"
)
