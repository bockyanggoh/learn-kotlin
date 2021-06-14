package com.example.contractconsumer.contracts

import au.com.dius.pact.consumer.dsl.DslPart
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.contractconsumer.ApiRequest
import com.example.contractconsumer.ApiResponse
import com.example.contractconsumer.ErrorResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.HashMap


class Contracts {

}