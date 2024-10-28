package com.hectordelgado.celestial.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class NasaApiErrorDto(
    val error: NasaError
) {
    @Serializable
    data class NasaError(
        val code: String,
        val message: String
    )
}
