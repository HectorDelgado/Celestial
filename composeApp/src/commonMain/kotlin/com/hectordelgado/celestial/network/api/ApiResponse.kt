package com.hectordelgado.celestial.network.api

import kotlinx.serialization.Serializable

sealed class ApiResponse<T>(
    data: T?,
    error: ApiError?
) {
    data class Success<T>(val data: T) : ApiResponse<T>(data, null)
    data class Error<T>(val error: ApiError) : ApiResponse<T>(null, error)
}

@Serializable
data class ApiError(
    val code: String,
    val message: String
)