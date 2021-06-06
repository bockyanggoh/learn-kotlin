package com.example.serialization.services

import com.example.serialization.models.EpochResponse
import com.example.serialization.models.SimpleResponse
import com.example.serialization.utils.SystemConstants
import mu.KotlinLogging
import org.springframework.stereotype.Component
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
}