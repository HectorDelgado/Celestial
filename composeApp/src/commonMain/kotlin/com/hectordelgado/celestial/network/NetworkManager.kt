package com.hectordelgado.celestial.network

import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.network.api.ApiResponse
import com.hectordelgado.celestial.network.response.NasaApiError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.prepareGet
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

    suspend inline fun <reified T> executeGetRequest(
        path: String,
        params: Map<String, String> = emptyMap()
    ): ApiResponse<T> {
        val response = client.prepareGet(path) {
            url {
                params.forEach { parameters.append(it.key, it.value) }
            }
        }.execute()

        return if (response.status == HttpStatusCode.OK) {
            ApiResponse.Success(response.body<T>())
        } else {
            val error = response.body<NasaApiError>()
            ApiResponse.Error(error.error)
        }
    }
}