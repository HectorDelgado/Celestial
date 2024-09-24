package com.hectordelgado.celestial.feature.imageoftheday

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.db.AppDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ImageOfTheDayScreenModel(
    private val nasaRepository: NasaRepository,
    private val appDatabase: AppDatabase
) : StateScreenModel<ImageOfTheDayState>(ImageOfTheDayState.empty) {

    fun fetchFavoritePictures() {
        screenModelScope.launch {
            val favorites = appDatabase.favoriteImageOfTheDayDao.selectAll()
            mutableState.value = state.value.copy(favorites = favorites)
        }
    }

    fun fetchPictureOfTheDay(offset: Long) {
        if (offset >= 0) {
            screenModelScope.launch {
                mutableState.value = mutableState.value
                    .copy(daysOffset = offset)
                val offsetDate = getDateFormatter().getDateAsYYYYMMDD(state.value.daysOffset)

                nasaRepository.fetchPictureOfTheDay(offsetDate)
                    .onStart {

                    }
                    .onEach {
                        mutableState.value = mutableState.value
                            .copy(
                                pictureOfTheDayDto = it.copy(
                                    isSaved = state.value
                                        .favorites
                                        .any { fav -> fav.imageUrl == it.url }
                                )
                            )
                    }
                    .catch {
                        getMLogger().logDebug("Error fetching picture of the day ${it.message}")
                    }
                    .launchIn(this)
            }
        }

    }

    fun onFavoriteClick(dto: PictureOfTheDayDto) {
        screenModelScope.launch {
            try {
                if (dto.isSaved) {
                    // remove from favorites
                    appDatabase.favoriteImageOfTheDayDao
                        .delete(dto.url)
                } else {
                    // add to favorites
                    appDatabase.favoriteImageOfTheDayDao
                        .insert(FavoriteImageOfTheDayEntity(dto))
                }

                mutableState.value = state.value
                    .copy(pictureOfTheDayDto = dto.copy(isSaved = !dto.isSaved))
            } catch (e: Exception) {
                getMLogger().logDebug("Error adding/removing favorite ${e.message}")
            }
        }
    }

}
