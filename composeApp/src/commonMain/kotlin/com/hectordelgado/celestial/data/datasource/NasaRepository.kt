package com.hectordelgado.celestial.data.datasource

import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import com.hectordelgado.celestial.network.dto.SolarFlareDto
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
}
