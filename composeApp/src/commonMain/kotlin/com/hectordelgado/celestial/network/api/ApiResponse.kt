package com.hectordelgado.celestial.network.api

import kotlinx.serialization.Serializable

sealed class ApiResponse<T>(
    open val data: T?,
    open val error: ApiError?
) {
    data class Success<T>(override val data: T) : ApiResponse<T>(data, null)
    data class Error<T>(override val error: ApiError) : ApiResponse<T>(null, error)
}

@Serializable
data class ApiError(
    val code: String,
    val message: String
)