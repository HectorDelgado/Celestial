package com.hectordelgado.celestial.network

import com.hectordelgado.celestial.network.api.ApiResponse
import com.hectordelgado.celestial.network.response.NasaApiError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.prepareDelete
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePost
import io.ktor.client.request.preparePut
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * This class is responsible for making requests to various APIs.
 */
class NetworkManager {

    val client = configureClient()

    private fun configureClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000
            }
        }
    }

    suspend inline fun <reified T> executeRequest(
        requestType: RequestType,
        path: String,
        params: Map<String, String> = emptyMap()
    ): ApiResponse<T> {
        val httpStatement = when (requestType) {
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

        val response = httpStatement.execute()

        return response.toApiResponse()
    }
}

enum class RequestType {
    GET,
    POST,
    PUT,
    DELETE
}

suspend inline fun <reified T> HttpResponse.toApiResponse(): ApiResponse<T> {
    return if (this.status == HttpStatusCode.OK) {
        ApiResponse.Success(this.body<T>())
    } else {
        val error = this.body<NasaApiError>()
        ApiResponse.Error(error.error)
    }
}
