package com.hectordelgado.celestial.feature.marsphotos

import com.hectordelgado.celestial.network.response.MarsPhoto

data class MarsPhotosState(
    val photos: List<MarsPhoto>,
    val daysOffset: Long = 0
) {
    companion object {
        val empty = MarsPhotosState(emptyList(), 0L)
    }
}
