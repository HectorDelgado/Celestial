package com.hectordelgado.celestial.data

import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.response.MarsPhotosResponse
import com.hectordelgado.celestial.network.response.PictureOfTheDayResponse
import com.hectordelgado.celestial.network.response.SolarFlareResponse
import kotlinx.coroutines.flow.Flow

class DefaultNasaRepository(private val nasaApi: NasaApi) : NasaRepository {
    override fun fetchPictureOfTheDay(
        date: String?,
        startDate: String?,
        endDate: String?,
        count: Int?
    ): Flow<PictureOfTheDayResponse> {
        return nasaApi.fetchPictureOfTheDay(date, startDate, endDate, count)
    }

    override fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareResponse>> {
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