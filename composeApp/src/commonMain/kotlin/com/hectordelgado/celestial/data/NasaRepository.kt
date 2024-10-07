package com.hectordelgado.celestial.data

import com.hectordelgado.celestial.network.response.MarsPhotosResponse
import com.hectordelgado.celestial.network.response.PictureOfTheDayResponse
import com.hectordelgado.celestial.network.response.SolarFlareResponse
import kotlinx.coroutines.flow.Flow

interface NasaRepository {
    fun fetchPictureOfTheDay(
        date: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        count: Int? = null
    ): Flow<PictureOfTheDayResponse>

    fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareResponse>>

    fun fetchMarsPhotos(
        date: String,
        page: Int = 1,
        rover: MarsPhotosResponse.Rover = MarsPhotosResponse.Rover.CURIOSITY
    ): Flow<MarsPhotosResponse>
}

