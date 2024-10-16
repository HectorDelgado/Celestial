package com.hectordelgado.celestial.data

import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.response.MarsPhotosResponse
import com.hectordelgado.celestial.network.response.PictureOfTheDayResponse
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

    override fun fetchMarsPhotos(
        date: String,
        page: Int,
        rover: MarsPhotosResponse.Rover
    ): Flow<MarsPhotosResponse> {
        return nasaApi.fetchMarsPhotos(date, page, rover)
    }
}