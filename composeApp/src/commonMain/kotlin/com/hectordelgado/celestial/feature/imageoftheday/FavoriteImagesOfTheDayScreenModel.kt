package com.hectordelgado.celestial.feature.imageoftheday

import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getNativeLogger
import com.hectordelgado.celestial.db.AppDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import com.hectordelgado.celestial.feature.core.app.ContentState
import com.hectordelgado.celestial.feature.core.app.ContentStateScreenModel
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import kotlinx.coroutines.launch

class FavoriteImagesOfTheDayScreenModel(
    private val appDatabase: AppDatabase
) : ContentStateScreenModel<FavoriteImagesOfTheDayState>(
    initialContentState = ContentState.Loading(),
    initialState = FavoriteImagesOfTheDayState.empty
) {
    fun loadFavorites() {
        screenModelScope.launch {
            mutableContentState.value = ContentState.Loading()

            val favorites = try {
                appDatabase.favoriteImageOfTheDayDao.selectAll()
            } catch (e: Exception) {
                getNativeLogger().logDebug("Error loading favorites ${e.message}")
                emptyList()
            }

            mutableState.value = FavoriteImagesOfTheDayState.empty.copy(favorites.map { ImageOfTheDay(it) })
            mutableContentState.value = ContentState.Success
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
                            selectedPreview = state.value.selectedPreview?.copy(isSaved = !item.isSaved)
                        )
                } else {
                    // add to favorites
                    appDatabase.favoriteImageOfTheDayDao
                        .insert(FavoriteImageOfTheDayEntity(item))

                    mutableState.value = state.value
                        .copy(
                            selectedPreview = state.value.selectedPreview?.copy(isSaved = !item.isSaved)
                        )
                }

            } catch (e: Exception) {
                getNativeLogger().logDebug("Error adding/removing favorite ${e.message}")
                SnackbarManager.showMessage("There was an error updating your favorites")
            }
        }
    }

    fun onItemClick(preview: ImageOfTheDay?) {
        mutableState.value = mutableState.value.copy(selectedPreview = preview)
    }
}