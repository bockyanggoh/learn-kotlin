package com.example.serialization.services

import com.example.serialization.InvalidIdException
import com.example.serialization.models.EpochRequest
import com.example.serialization.models.EpochResponse
import com.example.serialization.models.SimpleRequest
import com.example.serialization.models.SimpleResponse
import com.example.serialization.utils.SystemConstants
import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import java.lang.Exception

@Component
class SimpleService(
    private val restTemplate: RestTemplate
) {
    private val log = KotlinLogging.logger{}
    fun getRecord(id: Int): SimpleResponse? {
        try {
            val response = restTemplate.getForEntity(
                "${SystemConstants.baseUrl}/simple/$id",
                SimpleResponse::class.java
            )
            if (response.body != null)
                return response.body
            return null
        } catch (e: Exception) {
            log.error { e }
            return null
        }
    }

    fun getEpochRecord(id: Int): SimpleResponse? {
        try {
            val response = restTemplate.getForEntity(
                "${SystemConstants.baseUrl}/epoch/$id",
                EpochResponse::class.java
            )
            if (response.body != null)
                return SimpleResponse(response.body!!.id, response.body!!.name, response.body!!.dateTs)
            return null
        } catch (e: Exception) {
            log.error { e }
            return null
        }
    }

    fun createEpochRecord(req: SimpleRequest): SimpleResponse? {
        val headers = HttpHeaders()
        try {
            val response = restTemplate.exchange(
                "${SystemConstants.baseUrl}/epoch",
                HttpMethod.POST,
                HttpEntity(EpochRequest(req.id, req.name, req.dateTs), headers),
                EpochResponse::class.java
            )
            if (response.statusCode == HttpStatus.CREATED) {
                return this.getEpochRecord(req.id)
            }
        } catch (e: HttpServerErrorException) {
            throw InvalidIdException("Id [${req.id}] is already in use. Please try again with a valid id")
        }

        return null
    }

    fun createRecord(req: SimpleRequest): SimpleResponse? {
        val headers = HttpHeaders()
        try {
            val response = restTemplate.exchange(
                "${SystemConstants.baseUrl}/simple",
                HttpMethod.POST,
                HttpEntity(req, headers),
                SimpleResponse::class.java
            )
            if (response.statusCode == HttpStatus.CREATED) {
                return this.getRecord(req.id)
            }
        } catch (e: HttpServerErrorException) {
            throw InvalidIdException("Id [${req.id}] is already in use. Please try again with a valid id")
        }

        return null
    }
}