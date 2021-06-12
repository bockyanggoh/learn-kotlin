package com.example.contractconsumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.collections.HashMap

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor("APIProvider")
class ContractConsumerApplicationTests {
    @Pact(provider= "APIProvider", consumer = "consumer")
    fun validCreateResponse(builder: PactDslWithProvider): RequestResponsePact {
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        return builder
            .given("test POST")
            .uponReceiving("POST REQUEST")
            .path("/api/v1")
            .method("POST")
            .body(Json.encodeToString(ApiRequest("test", "test2")))
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body(Json.encodeToString(ApiResponse(UUID.randomUUID().toString(), "test", "test2", "testtest2")))
            .toPact()
    }

    @Test
    internal fun post_Success(server: MockServer) {
        val restTemplate = RestTemplate(
            listOf(KotlinSerializationJsonHttpMessageConverter())
        )
        val headers = org.springframework.http.HttpHeaders()

        val response = restTemplate.exchange(
            server.getUrl() + "/api/v1",
            HttpMethod.POST,
            HttpEntity(ApiRequest("test", "test2"), headers),
            ApiResponse::class.java
        )
        Assertions.assertEquals(response.statusCode, HttpStatus.OK)
    }

    @Test
    internal fun post_Failure(server: MockServer) {
        val restTemplate = RestTemplate(
            listOf(KotlinSerializationJsonHttpMessageConverter())
        )
        val headers = org.springframework.http.HttpHeaders()

        val response = restTemplate.exchange(
            server.getUrl() + "/api/v1",
            HttpMethod.POST,
            HttpEntity(ApiRequest(null, null), headers),
            ApiResponse::class.java
        )
        Assertions.assertEquals(response.statusCode, HttpStatus.OK)
    }


}