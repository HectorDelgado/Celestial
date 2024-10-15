package com.hectordelgado.celestial.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDayResponse(
    val date: String,
    val explanation: String,
    val title: String,
    val url: String,
    @SerialName("media_type") val mediaType: String,
    val copyright: String? = null,
)
