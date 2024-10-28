package com.hectordelgado.celestial.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDayDto(
    val date: String,
    val explanation: String,
    val title: String,
    val url: String,
    @SerialName("media_type") val mediaType: String,
    val copyright: String? = null,
)
