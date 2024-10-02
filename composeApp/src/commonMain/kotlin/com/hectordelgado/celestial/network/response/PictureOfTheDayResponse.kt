package com.hectordelgado.celestial.network.response

import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDayResponse(
    val copyright: String,
    val date: String,
    val explanation: String,
    val title: String,
    val url: String
)