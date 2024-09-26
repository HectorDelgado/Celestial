package com.hectordelgado.celestial.network.api

import com.hectordelgado.celestial.BuildKonfig
import com.hectordelgado.celestial.network.NetworkManager
import com.hectordelgado.celestial.network.model.PictureOfTheDayDto
import com.hectordelgado.celestial.network.model.SolarFlareDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * This class is responsible for making requests to the NASA API.
 *
 * @param networkManager The network manager used to make the requests.
 */
class NasaApi(private val networkManager: NetworkManager) {
    private val baseUrl = "https://api.nasa.gov"
    private val api_Key = BuildKonfig.NASA_API_KEY

    /**
     * Makes a GET request to the NASA API.
     *
     * @param path The path of the request.
     * @param params The parameters of the request.
     */
    private suspend inline fun <reified T> makeRequest(
        path: String,
        params: Map<String, String> = emptyMap()
    ): ApiResponse<T> {
        return networkManager.executeGetRequest<T>(
            "$baseUrl/$path",
            params.toMutableMap().apply { put("api_key", api_Key) }
        )
    }

    private suspend fun <T> handleResponseAsFlow(response: ApiResponse<T>): Flow<T> = flow {
        when (response) {
            is ApiResponse.Success<T> -> {
                emit(response.data)
            }

            is ApiResponse.Error -> {
                throw Exception(response.error.message)
            }
        }
    }

    /**
     * Fetches the picture of the day from the NASA API.
     *
     * @param date The date of the picture to fetch.
     * @param startDate The start date of the range of pictures to fetch.
     * @param endDate The end date of the range of pictures to fetch.
     * @param count The number of pictures to fetch.
     */
    fun fetchPictureOfTheDay(
        date: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        count: Int? = null
    ): Flow<PictureOfTheDayDto> = flow {
        val params = mutableMapOf<String, String>()
        date?.let { params.put("date", it) }
        startDate?.let { params.put("start_date", it) }
        endDate?.let { params.put("end_date", it) }
        count?.let { params.put("count", it.toString()) }

        val response = makeRequest<PictureOfTheDayDto>(
            "planetary/apod",
            params
        )

        emitAll(handleResponseAsFlow(response))
    }


    /**
     * Fetches the solar flare data from the NASA API.
     *
     * @param startDate The start date of the range of solar flare data to fetch.
     * @param endDate The end date of the range of solar flare data to fetch.
     */
    fun fetchSolarFlareData(
        startDate: String?,
        endDate: String?
    ): Flow<List<SolarFlareDto>> {
        return flow {
            val params = mutableMapOf<String, String>()
            startDate?.let { params.put("startDate", it) }
            endDate?.let { params.put("endDate", it) }

            val response = makeRequest<List<SolarFlareDto>>(
                    "DONKI/FLR",
                    params = params
                )

            emitAll(handleResponseAsFlow(response))
        }
    }
}
