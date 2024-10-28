package com.hectordelgado.celestial.network.api

sealed class ApiResponse<T>(
    open val data: T?,
    open val apiError: ApiError?
) {
    data class Success<T>(override val data: T) : ApiResponse<T>(data, null)
    data class Error<T>(override val apiError: ApiError) : ApiResponse<T>(null, apiError)
}

data class ApiError(
    override val message: String,
    val code: String? = null
) : Error(message)
