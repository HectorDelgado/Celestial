package com.hectordelgado.celestial.network

import com.hectordelgado.celestial.network.api.ApiError
import com.hectordelgado.celestial.network.api.ApiResponse
import com.hectordelgado.celestial.network.dto.NasaApiErrorDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.prepareDelete
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePost
import io.ktor.client.request.preparePut
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * This class is responsible for making requests to various APIs.
 */
object NetworkManager {
    enum class RequestType {
        GET,
        POST,
        PUT,
        DELETE
    }

    private val client = configureClient()

    private fun configureClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }

    /**
     * Prepares a request to the given path with the given parameters.
     */
    @PublishedApi
    internal suspend fun prepareRequest(
        requestType: RequestType,
        path: String,
        params: Map<String, String> = emptyMap()
    ): HttpStatement {
        return when (requestType) {
            RequestType.GET -> client.prepareGet(path) {
                url {
                    params.forEach { parameters.append(it.key, it.value) }
                }
            }

            RequestType.POST -> client.preparePost(path) {
                url {
                    params.forEach { parameters.append(it.key, it.value) }
                }
            }

            RequestType.PUT -> client.preparePut(path) {
                url {
                    params.forEach { parameters.append(it.key, it.value) }
                }
            }

            RequestType.DELETE -> client.prepareDelete(path) {
                url {
                    params.forEach { parameters.append(it.key, it.value) }
                }
            }
        }
    }

    /**
     * Safely handles a request and parses the response into a [ApiResponse].
     */
    @PublishedApi
    internal suspend inline fun <reified T> handleRequest(
        httpStatement: HttpStatement
    ): ApiResponse<T> {
        return try {
            val response = httpStatement.execute()
            when(response.status) {
                HttpStatusCode.OK -> {
                    ApiResponse.Success(response.body<T>())
                }
                HttpStatusCode.Forbidden -> {
                    val body = response.bodyAsText()
                    val parsedError = try {
                        val errorDto = Json.decodeFromString<NasaApiErrorDto>(body)
                        ApiError(message = errorDto.error.message, code = errorDto.error.code)
                    } catch (e: Exception) {
                        ApiError(message = "Network error: $e", code = "${response.status.value}")
                    }

                    ApiResponse.Error(parsedError)
                }
                else -> {
                    ApiResponse.Error(
                        ApiError(
                            "Unhandled network error: ${response.status}",
                            "${response.status.value}"
                        )
                    )
                }
            }
        } catch (e: SerializationException) {
            ApiResponse.Error(ApiError("Serialization exception: ${e.message}"))
        } catch (e: Exception) {
            ApiResponse.Error(ApiError("Network error: ${e.message}"))
        }
    }

    suspend inline fun <reified T> executeRequest(
        requestType: RequestType,
        path: String,
        params: Map<String, String>
    ): ApiResponse<T> {
        val httpStatement = prepareRequest(requestType, path, params)
        return handleRequest<T>(httpStatement)
    }

    inline fun <reified T> executeRequestAsFlow(
        requestType: RequestType,
        path: String,
        params: Map<String, String>
    ): Flow<T> = flow {
        when (val response = executeRequest<T>(requestType, path, params)) {
            is ApiResponse.Success<T> -> {
                emit(response.data)
            }

            is ApiResponse.Error -> {
                throw response.apiError
            }
        }
    }
}
