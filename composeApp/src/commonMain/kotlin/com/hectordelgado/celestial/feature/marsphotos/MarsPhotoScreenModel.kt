package com.hectordelgado.celestial.feature.marsphotos

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getNativeLogger
import com.hectordelgado.celestial.data.NasaRepository
import com.hectordelgado.celestial.feature.core.app.ContentState
import com.hectordelgado.celestial.feature.core.app.ContentStateScreenModel
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarState
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.network.dto.MarsPhoto
import com.hectordelgado.celestial.network.dto.MarsPhotosDto
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MarsPhotoScreenModel(
    private val nasaRepository: NasaRepository
) : ContentStateScreenModel<MarsPhotosState>(MarsPhotosState.empty, ContentState.Loading()) {

    fun onMarsPhotosRequested(daysOffset: Long) {
        screenModelScope.launch {
            // update at end

            val date = getDateFormatter().getCurrentDateAsYYYYMMDD(365 + daysOffset)

            nasaRepository.fetchMarsPhotos(date, state.value.page, state.value.selectedRover)
                .onStart {
                    mutableContentState.value = ContentState.Loading()
                    mutableState.value = state.value.copy(daysOffset = daysOffset, page = 1)
                    TopBarManager.updateState {
                        setIsVisible(false)
                    }
                }
                .onEach {
                    mutableState.value = state.value.copy(photos = it.photos)
                }
                .catch {
                    getNativeLogger().logDebug("Error fetching mars photos: ${it.message}")
                    SnackbarManager.showMessage(
                        SnackbarState(
                            message = "Error fetching mars photos",
                            actionLabel = "Retry",
                            duration = SnackbarDuration.Long,
                            onSnackbarResult = {
                                if (it == SnackbarResult.ActionPerformed) {
                                    onMarsPhotosRequested(state.value.daysOffset)
                                }
                            }
                        )
                    )
                }
                .onCompletion {
                    mutableContentState.value = ContentState.Success
                    TopBarManager.updateState {
                        setIsVisible(true)
                        setTitle(date)
                        setLeftIcon(TopBarLeftIcon.BACK)
                    }
                }
                .launchIn(this)
        }
    }

    fun onRoverSelected(rover: MarsPhotosDto.Rover) {
        if (state.value.selectedRover != rover) {
            mutableState.value = state.value.copy(selectedRover = rover)
            onMarsPhotosRequested(state.value.daysOffset)
        }
    }

    fun onLoadNextPage(currentPage: Int) {
        //mutableState.value = state.value.copy(page = state.value.page + 1)

        screenModelScope.launch {
            // update at end

            val date = getDateFormatter().getCurrentDateAsYYYYMMDD(365 + state.value.daysOffset)

            nasaRepository.fetchMarsPhotos(date, state.value.page,  state.value.selectedRover)
                .onStart {
                    mutableState.value = state.value
                        .copy(
                            isLoadingAdditionalPhotos = true,
                            page = currentPage + 1
                        )
                }
                .onEach {
                    mutableState.value = state.value.copy(photos = state.value.photos + it.photos)
                }
                .catch {
                    SnackbarManager.showMessage(
                        SnackbarState(
                            message = "Error fetching more photos",
                            actionLabel = "Retry",
                            duration = SnackbarDuration.Long,
                            onSnackbarResult = {
                                if (it == SnackbarResult.ActionPerformed) {
                                    //onMarsPhotosRequested(state.value.daysOffset)
                                    onLoadNextPage(currentPage - 1)
                                }

                            }
                        )
                    )
                    getNativeLogger().logDebug("Error fetching mars photos: ${it.message}")
                }
                .onCompletion {
                    mutableState.value = state.value.copy(isLoadingAdditionalPhotos = false)
                    TopBarManager.updateState {
                        setIsVisible(true)
                        setTitle(date)
                        setLeftIcon(TopBarLeftIcon.BACK)
                    }
                }
                .launchIn(this)
        }
    }

    fun onMarsPhotoSelected(photo: MarsPhoto?) {
        mutableState.value = state.value.copy(selectedMarsPhoto = photo)
    }
}