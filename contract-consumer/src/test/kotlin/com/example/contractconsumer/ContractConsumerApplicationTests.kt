package com.example.contractconsumer

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.collections.HashMap

@ExtendWith(PactConsumerTestExt::class)
class ContractConsumerApplicationTests {

    @Pact(consumer = "consumer")
    fun validCreateResponse(builder: PactDslWithProvider): RequestResponsePact {
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        return builder
            .given("test POST")
            .uponReceiving("POST REQUEST")
            .path("/api/v1/")
            .method("POST")
            .body(Json.encodeToString(ApiRequest("test", "test2")))
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body(Json.encodeToString(ApiResponse(UUID.randomUUID().toString(), "test", "test2", "testtest2")))
            .toPact()
    }

    @Test
    fun contextLoads() {
    }

}
