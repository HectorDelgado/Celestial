package com.hectordelgado.celestial.feature.marsphotos

import com.hectordelgado.celestial.network.dto.MarsPhoto
import com.hectordelgado.celestial.network.dto.MarsPhotosDto

data class MarsPhotosState(
    val daysOffset: Long,
    val page: Int,
    val isLoadingAdditionalPhotos: Boolean,
    val selectedRover: MarsPhotosDto.Rover,
    val selectedMarsPhoto: MarsPhoto? = null,
    val photos: List<MarsPhoto>
) {
    companion object {
        val empty = MarsPhotosState(
            daysOffset = 0L,
            page = 1,
            isLoadingAdditionalPhotos = false,
            selectedRover = MarsPhotosDto.Rover.CURIOSITY,
            selectedMarsPhoto = null,
            photos = emptyList()
        )
    }
}
