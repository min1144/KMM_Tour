package com.test.examplekmp.data.network

import com.test.examplekmp.domain.exception.ServerResponseError
import com.test.examplekmp.domain.util.delegate.AppConfigDelegate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class HttpClientFactory constructor(
    private val appConfig: AppConfigDelegate,
    private val client: HttpClient,
    private val logger: Logger = Logger.DEFAULT,
    private val host: String,
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun create(): HttpClient = client.config {
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            connectTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            socketTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
        }
        install(ContentNegotiation) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Accept, "*/*")
                append(HttpHeaders.AcceptCharset, "utf-8")
            }

            register(
                ContentType.Text.Xml, KotlinxSerializationConverter(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            )

            json(Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                explicitNulls = false
            })
        }
        install(Logging) {
            logger = this@HttpClientFactory.logger
            level = LogLevel.ALL
        }
        defaultRequest {
            url {
                protocol = URLProtocol.Companion.HTTPS
                this.host = this@HttpClientFactory.host
            }
        }
        HttpResponseValidator {
            validateResponse {
                val status = it.status
                if (appConfig.debug) {
                    logger.log("response status $status >> ${it.request.url}")
                }
                if (status != HttpStatusCode.OK) {
                    val statusCode = it.status.value
                    val errorBody: String? = try {
                        it.body()
                    } catch (e: Exception) {
                        null
                    }
                    throw ServerResponseError(
                        statusCode,
                        errorBody, ServerResponseException(it, "Server Response Error")
                    )
                }
            }
        }
    }
}