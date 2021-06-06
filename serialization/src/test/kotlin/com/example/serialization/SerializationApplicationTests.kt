package com.example.serialization

import com.example.serialization.models.SimpleResponse
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest
class SerializationApplicationTests(
) {
    companion object {
        val log = KotlinLogging.logger(this::class.java.name)
    }
    private val testRestTemplate: TestRestTemplate = TestRestTemplate()
    @Test
    internal fun testCanReturn() {
        val response = testRestTemplate.getForEntity(
            "http://localhost:8080/simple/1",
            SimpleResponse::class.java
        )
        log.info{response.body}
    }
}
