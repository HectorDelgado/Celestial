package com.hectordelgado.celestial.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDayDto(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val title: String,
    val url: String,
    val isSaved: Boolean = false
)
