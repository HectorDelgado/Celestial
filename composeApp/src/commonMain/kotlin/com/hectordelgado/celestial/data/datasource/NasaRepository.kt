package com.hectordelgado.celestial.data.datasource

import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.response.MarsPhotosResponse
import com.hectordelgado.celestial.network.response.PictureOfTheDayDto
import com.hectordelgado.celestial.network.response.SolarFlareDto
import kotlinx.coroutines.flow.Flow

interface NasaRepository {
    fun fetchPictureOfTheDay(
        date: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        count: Int? = null
    ): Flow<PictureOfTheDayDto>

    fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareDto>>

    fun fetchMarsPhotos(
        date: String,
        page: Int = 1,
        rover: MarsPhotosResponse.Rover = MarsPhotosResponse.Rover.CURIOSITY
    ): Flow<MarsPhotosResponse>
}

class DefaultNasaRepository(private val nasaApi: NasaApi) : NasaRepository {
    override fun fetchPictureOfTheDay(
        date: String?,
        startDate: String?,
        endDate: String?,
        count: Int?
    ): Flow<PictureOfTheDayDto> {
        return nasaApi.fetchPictureOfTheDay(date, startDate, endDate, count)
    }

    override fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareDto>> {
        return nasaApi.fetchSolarFlareData(startDate, endDate)
    }

    override fun fetchMarsPhotos(
        date: String,
        page: Int,
        rover: MarsPhotosResponse.Rover
    ): Flow<MarsPhotosResponse> {
        return nasaApi.fetchMarsPhotos(date, page, rover)
    }

}
