package com.hectordelgado.celestial.network.response

import com.hectordelgado.celestial.network.api.ApiError
import kotlinx.serialization.Serializable

@Serializable
data class NasaApiError(
    val error: ApiError
)


