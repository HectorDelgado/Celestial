package com.hectordelgado.celestial.feature.marsphotos

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarState
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.network.response.MarsPhoto
import com.hectordelgado.celestial.network.response.MarsPhotosResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MarsPhotoScreenModel(
    private val nasaRepository: NasaRepository
) : StateScreenModel<MarsPhotosState>(MarsPhotosState.empty) {

    fun onMarsPhotosRequested(daysOffset: Long) {
        screenModelScope.launch {
            // update at end
            mutableState.value = state.value.copy(daysOffset = daysOffset, page = 1)
            val date = getDateFormatter().getCurrentDateAsYYYYMMDD(365 + daysOffset)

            nasaRepository.fetchMarsPhotos(date, page = state.value.page, rover = state.value.selectedRover)
                .onStart {
                    TopBarManager.updateState {
                        setIsVisible(false)
                    }
                    mutableState.value = state.value.copy(isLoading = true, pageInitialized = false)
                }
                .onEach {
                    mutableState.value = state.value.copy(photos = it.photos, pageInitialized = true)
                }
                .catch {
                    getMLogger().logDebug("Error fetching mars photos: ${it.message}")
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
                    mutableState.value = state.value.copy(isLoading = false)
                    TopBarManager.updateState {
                        setIsVisible(true)
                        setTitle(date)
                        setLeftIcon(TopBarLeftIcon.BACK)
                    }
                }
                .launchIn(this)
        }
    }

    fun onRoverSelected(rover: MarsPhotosResponse.Rover) {
        if (state.value.selectedRover != rover) {
            mutableState.value = state.value.copy(selectedRover = rover)
            onMarsPhotosRequested(state.value.daysOffset)
        }
    }

    fun onLoadNextPage() {
        mutableState.value = state.value.copy(page = state.value.page + 1)

        screenModelScope.launch {
            // update at end

            val date = getDateFormatter().getCurrentDateAsYYYYMMDD(365 + state.value.daysOffset)

            nasaRepository.fetchMarsPhotos(date, page = state.value.page, rover = state.value.selectedRover)
                .onStart {
                    mutableState.value = state.value.copy(isLoading = true)
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
                                    onMarsPhotosRequested(state.value.daysOffset)
                                }

                            }
                        )
                    )
                    getMLogger().logDebug("Error fetching mars photos: ${it.message}")
                }
                .onCompletion {
                    mutableState.value = state.value.copy(isLoading = false)
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