package com.hectordelgado.celestial.data

import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.dto.MarsPhotosDto
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import kotlinx.coroutines.flow.Flow

class DefaultNasaRepository(private val nasaApi: NasaApi) : NasaRepository {
    override fun fetchPictureOfTheDay(
        date: String?,
        startDate: String?,
        endDate: String?,
        count: Int?
    ): Flow<PictureOfTheDayDto> {
        return nasaApi.fetchPictureOfTheDay(date, startDate, endDate, count)
    }

    override fun fetchMarsPhotos(
        date: String,
        page: Int,
        rover: MarsPhotosDto.Rover
    ): Flow<MarsPhotosDto> {
        return nasaApi.fetchMarsPhotos(date, page, rover)
    }
}