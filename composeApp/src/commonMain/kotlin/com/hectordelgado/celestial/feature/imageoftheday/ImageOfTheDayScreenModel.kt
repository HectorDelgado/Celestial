package com.hectordelgado.celestial.feature.imageoftheday

import androidx.compose.material.SnackbarResult
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.NasaRepository
import com.hectordelgado.celestial.db.DefaultAppDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import com.hectordelgado.celestial.feature.core.app.ContentState
import com.hectordelgado.celestial.feature.core.app.ContentStateScreenModel
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarState
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ImageOfTheDayScreenModel(
    private val nasaRepository: NasaRepository,
    private val appDatabase: DefaultAppDatabase
) : ContentStateScreenModel<ImageOfTheDayState>(ImageOfTheDayState.empty, ContentState.Loading()) {

    fun fetchFavoritePictures() {
        screenModelScope.launch {
            val favorites = appDatabase.favoriteImageOfTheDayDao.selectAll()
            mutableState.value = state.value.copy(favorites = favorites.map { ImageOfTheDay(it) })
        }
    }

    fun fetchPictureOfTheDay(offset: Long) {
        if (offset >= 0) {
            screenModelScope.launch {
                mutableState.value = mutableState.value.copy(daysOffset = offset)
                val offsetDate = getDateFormatter().getCurrentDateAsYYYYMMDD(state.value.daysOffset)

                nasaRepository.fetchPictureOfTheDay(offsetDate)
                    .onStart {
                        TopBarManager.updateState {
                            setIsVisible(false)
                        }
                    }
                    .onEach { dto ->
                        mutableContentState.value = ContentState.Success
                        mutableState.value = mutableState.value
                            .copy(
                                imageOfTheDay = ImageOfTheDay(dto)
                                    .copy(isSaved = state.value.favorites.any { it.imageUrl ==  dto.url})
                            )
                    }
                    .catch {
                        mutableContentState.value = ContentState.Error()
                        SnackbarManager.showMessage(
                            SnackbarState(
                                message = "Error fetching picture of the day",
                                actionLabel = "Retry",
                                onSnackbarResult = {
                                    if (it == SnackbarResult.ActionPerformed) {
                                        fetchPictureOfTheDay(state.value.daysOffset)
                                    }
                                }
                            )
                        )
                        getMLogger().logDebug("Error fetching picture of the day ${it.message}")
                    }
                    .onCompletion {
                        TopBarManager.updateState {
                            setIsVisible(true)
                            setTitle("Image of the day")
                            setLeftIcon(TopBarLeftIcon.BACK)
                        }
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
