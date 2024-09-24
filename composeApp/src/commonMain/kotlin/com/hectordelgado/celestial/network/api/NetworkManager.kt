package com.hectordelgado.celestial.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.prepareGet
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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
        }
    }

    /**
     * Wrapper function for making requests to the NASA API.
     */
    suspend inline fun <reified T> makeGetRequest(
        path: String,
        params: Map<String, String> = emptyMap(),
        //block: HttpRequestBuilder.() -> Unit = {} // not needed now but could be useful in the future
    ): T {
        return client
            .prepareGet(path) {
                url {
                    params.forEach {parameters.append(it.key, it.value) }
                }
                //this.apply(block)
            }
            .execute()
            .body<T>()
    }
}