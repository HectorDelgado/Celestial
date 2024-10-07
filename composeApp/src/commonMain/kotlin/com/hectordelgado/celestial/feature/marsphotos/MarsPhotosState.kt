package com.hectordelgado.celestial.feature.marsphotos

import com.hectordelgado.celestial.network.response.MarsPhoto
import com.hectordelgado.celestial.network.response.MarsPhotosResponse

data class MarsPhotosState(
    val daysOffset: Long,
    val page: Int,
    val isLoadingAdditionalPhotos: Boolean,
    val selectedRover: MarsPhotosResponse.Rover,
    val selectedMarsPhoto: MarsPhoto? = null,
    val photos: List<MarsPhoto>
) {
    companion object {
        val empty = MarsPhotosState(
            daysOffset = 0L,
            page = 1,
            isLoadingAdditionalPhotos = false,
            selectedRover = MarsPhotosResponse.Rover.CURIOSITY,
            selectedMarsPhoto = null,
            photos = emptyList()
        )
    }
}
