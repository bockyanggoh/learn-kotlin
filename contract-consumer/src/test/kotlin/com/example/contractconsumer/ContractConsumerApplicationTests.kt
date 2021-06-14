package com.example.contractconsumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue
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
import org.springframework.http.MediaType
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            .given("Valid Request Body")
            .uponReceiving("POST REQUEST")
            .path("/api/v1")
            .matchHeader(
                "Content-Type", "application/json+", "application/json"
            )
            .method("POST")
            .body(
                PactDslJsonBody()
                    .stringMatcher("name", "\\S{4,50}", "test")
                    .stringMatcher("value1", ".+", "test"))
            .willRespondWith()
            .status(200)
            .headers(headers)
            .body(
                PactDslJsonBody()
                    .stringMatcher("name", "\\S{4,50}", "test")
                    .stringMatcher("value1", ".+", "test")
                    .stringMatcher("value2", ".+", "test")
                    .stringMatcher("id", "\\S{36}", UUID.randomUUID().toString())
            )
            .uponReceiving("Error Request with Null in [name]")
            .path("/api/v1")
            .matchHeader(
                "Content-Type", "application/json+", "application/json"
            )
            .method("POST")
            .body(
                PactDslJsonBody()
                    .stringMatcher("value1", ".+", "test")
                    .stringMatcher("name", "^$", "")
            )
            .willRespondWith()
            .status(400)
            .headers(headers)
            .body(
                PactDslJsonBody()
                    .uuid("id")
                    .stringType("errorCode", "NoRecordFound")
                    .stringType("transactionTs", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .stringType("errorDescription", "No Code Found")
            )
            .toPact()
    }

    @Test
    internal fun post_Success(server: MockServer) {
        val restTemplate = RestTemplate(
            listOf(KotlinSerializationJsonHttpMessageConverter())
        )
        val headers = org.springframework.http.HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

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
        headers.contentType = MediaType.APPLICATION_JSON

        val response = restTemplate.exchange(
            server.getUrl() + "/api/v1",
            HttpMethod.POST,
            HttpEntity(ApiRequest("test", ""), headers),
            ApiResponse::class.java
        )
        print(response.body.toString())
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}