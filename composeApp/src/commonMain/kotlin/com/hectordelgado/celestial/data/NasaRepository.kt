package com.hectordelgado.celestial.data

import com.hectordelgado.celestial.network.dto.MarsPhotosDto
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import kotlinx.coroutines.flow.Flow

interface NasaRepository {
    fun fetchPictureOfTheDay(
        date: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        count: Int? = null
    ): Flow<PictureOfTheDayDto>

    fun fetchMarsPhotos(
        date: String,
        page: Int = 1,
        rover: MarsPhotosDto.Rover = MarsPhotosDto.Rover.CURIOSITY
    ): Flow<MarsPhotosDto>
}

