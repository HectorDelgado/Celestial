package com.hectordelgado.celestial.network.api

import com.hectordelgado.celestial.BuildKonfig
import com.hectordelgado.celestial.network.NetworkManager
import com.hectordelgado.celestial.network.dto.MarsPhotosDto
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import kotlinx.coroutines.flow.Flow

/**
 * This class is responsible for making requests to the NASA API.
 *
 * @param networkManager The network manager used to make the requests.
 */
class NasaApi(private val networkManager: NetworkManager) {
    private val baseUrl = "https://api.nasa.gov"
    private val apiKey = BuildKonfig.NASA_API_KEY

    /**
     * Makes a GET request to the NASA API.
     *
     * @param path The path of the request.
     * @param params The parameters of the request.
     */
    private inline fun <reified T> makeRequestAsFlow(
        requestType: NetworkManager.RequestType,
        path: String,
        params: Map<String, String> = emptyMap()
    ): Flow<T> {
        return networkManager.executeRequestAsFlow<T>(
            requestType,
            "$baseUrl/$path",
            params.toMutableMap().apply { put("api_key", apiKey) }
        )
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
    ): Flow<PictureOfTheDayDto> {
        val params = mutableMapOf<String, String>()
        date?.let { params.put("date", it) }
        startDate?.let { params.put("start_date", it) }
        endDate?.let { params.put("end_date", it) }
        count?.let { params.put("count", it.toString()) }

        return makeRequestAsFlow<PictureOfTheDayDto>(
            NetworkManager.RequestType.GET,
            "planetary/apod",
            params
        )
    }

    fun fetchMarsPhotos(
        date: String,
        page: Int = 1,
        rover: MarsPhotosDto.Rover = MarsPhotosDto.Rover.CURIOSITY
    ): Flow<MarsPhotosDto> {
        val params = mutableMapOf<String, String>()
        params["earth_date"] = date
        params["page"] = page.toString()

        return makeRequestAsFlow<MarsPhotosDto>(
            NetworkManager.RequestType.GET,
            "mars-photos/api/v1/rovers/${rover.value}/photos",
            params = params
        )
    }
}
