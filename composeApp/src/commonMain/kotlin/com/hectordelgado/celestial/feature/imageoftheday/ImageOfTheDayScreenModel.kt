package com.hectordelgado.celestial.feature.imageoftheday

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.db.AppDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
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
            mutableState.value = state.value.copy(favorites = favorites.map { ImageOfTheDay(it) })
        }
    }

    fun fetchPictureOfTheDay(offset: Long) {
        if (offset >= 0) {
            screenModelScope.launch {
                mutableState.value = mutableState.value
                    .copy(daysOffset = offset)
                val offsetDate = getDateFormatter().getCurrentDateAsYYYYMMDD(state.value.daysOffset)

                nasaRepository.fetchPictureOfTheDay(offsetDate)
                    .onStart {

                    }
                    .onEach { dto ->
                        mutableState.value = mutableState.value
                            .copy(
                                imageOfTheDay = ImageOfTheDay(dto)
                                    .copy(isSaved = state.value.favorites.any { it.imageUrl ==  dto.url})
                            )
                    }
                    .catch {
                        getMLogger().logDebug("Error fetching picture of the day ${it.message}")
                    }
                    .launchIn(this)
            }
        }

    }

    fun onFavoriteClick(item: ImageOfTheDay) {
        screenModelScope.launch {
            try {
                if (item.isSaved) {
                    // remove from favorites
                    appDatabase.favoriteImageOfTheDayDao
                        .delete(item.imageUrl)

                    mutableState.value = state.value
                        .copy(
                            imageOfTheDay = state.value.imageOfTheDay?.copy(isSaved = false),
                            favorites = state.value.favorites.filter { it.imageUrl != item.imageUrl }
                        )
                } else {
                    // add to favorites
                    appDatabase.favoriteImageOfTheDayDao
                        .insert(FavoriteImageOfTheDayEntity(item))

                    mutableState.value = state.value
                        .copy(
                            imageOfTheDay = state.value.imageOfTheDay?.copy(isSaved = true),
                            favorites = state.value.favorites + item
                        )
                }

            } catch (e: Exception) {
                getMLogger().logDebug("Error adding/removing favorite ${e.message}")
            }
        }
    }

}
