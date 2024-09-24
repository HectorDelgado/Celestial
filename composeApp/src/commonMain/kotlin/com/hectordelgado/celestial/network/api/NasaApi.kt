package com.hectordelgado.celestial.network.api

import com.hectordelgado.celestial.BuildKonfig
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import com.hectordelgado.celestial.network.dto.SolarFlareDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NasaApi(private val networkManager: NetworkManager) {
    private val baseUrl = "https://api.nasa.gov"
    private val api_Key = BuildKonfig.NASA_API_KEY

    private suspend inline fun <reified T> makeRequest(
        path: String,
        params: Map<String, String> = emptyMap()
    ): T {
        return networkManager.makeGetRequest<T>(
            "$baseUrl/$path",
            params.toMutableMap().apply { put("api_key", api_Key) }
        )
    }

    fun fetchPictureOfTheDay(
        date: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        count: Int? = null
    ): Flow<PictureOfTheDayDto> {
        return flow {
            val params = mutableMapOf<String, String>()
            date?.let { params.put("date", it) }
            startDate?.let { params.put("start_date", it) }
            endDate?.let { params.put("end_date", it) }
            count?.let { params.put("count", it.toString()) }

            val response = makeRequest<PictureOfTheDayDto>(
                "planetary/apod",
                params
            )

            emit(response)
        }
    }

    fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareDto>> {
        return flow {
            val params = mutableMapOf<String, String>()
            startDate?.let { params.put("start_date", it) }
            endDate?.let { params.put("end_date", it) }


            val response = networkManager
                .makeGetRequest<List<SolarFlareDto>>(
                    "DONKI/FLR",
                    params = params
                )
                .map {
                    // move this to presentation layer
                    it.copy(
                        beginTime = getDateFormatter().formatAsDayMonthYear(it.beginTime),
                        peakTime = getDateFormatter().formatAsDayMonthYear(it.peakTime),
                        endTime = it.endTime?.let { getDateFormatter().formatAsDayMonthYear(it) }
                    )
                }
            emit(response)
        }
    }
}

