package com.hectordelgado.celestial.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolarFlareDto(
    val flrID: String,
    val instruments: List<Instrument>,
    val beginTime: String,
    val peakTime: String,
    val endTime: String? = null,
    val classType: String,
    val sourceLocation: String,
    val activeRegionNum: Int?,
    val note: String? = null,
    val submissionTime: String,
    val versionId: Int,
    val link: String,
    val linkedEvents: List<LinkedEvent>? = null
)

@Serializable
data class Instrument(
    val displayName: String
)

@Serializable
data class LinkedEvent(
    val activityID: String
)
