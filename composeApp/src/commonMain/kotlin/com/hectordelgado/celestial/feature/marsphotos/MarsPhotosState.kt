package com.hectordelgado.celestial.feature.marsphotos

import com.hectordelgado.celestial.network.response.MarsPhoto
import com.hectordelgado.celestial.network.response.MarsPhotosResponse

data class MarsPhotosState(
    val pageInitialized: Boolean,
    val daysOffset: Long,
    val page: Int,
    val isLoading: Boolean,
    val selectedRover: MarsPhotosResponse.Rover,
    val selectedMarsPhoto: MarsPhoto? = null,
    val photos: List<MarsPhoto>
) {
    companion object {
        val empty = MarsPhotosState(
            pageInitialized = false,
            daysOffset = 0L,
            page = 1,
            isLoading = false,
            selectedRover = MarsPhotosResponse.Rover.CURIOSITY,
            selectedMarsPhoto = null,
            photos = emptyList()
        )
    }
}
