package com.hectordelgado.celestial.feature.marsphotos

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.feature.core.app.BaseScreenModel
import com.hectordelgado.celestial.feature.core.app.ScreenState
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MarsPhotoScreenModel(
    private val nasaRepository: NasaRepository
) : BaseScreenModel<MarsPhotosState>(MarsPhotosState.empty, ScreenState.Loading()) {

    fun onMarsPhotosRequested(daysOffset: Long) {
        screenModelScope.launch {
            // update at end
            mutableState.value = state.value.copy(daysOffset = daysOffset)
            val date = getDateFormatter().getCurrentDateAsYYYYMMDD(365 + daysOffset)

            nasaRepository.fetchMarsPhotos(date)
                .onStart {
                    mutableScreenState.value = ScreenState.Loading()
                }
                .onEach {
                    mutableScreenState.value = ScreenState.Success
                    mutableState.value = state.value.copy(photos = it.photos)
                }
                .catch {
                    mutableScreenState.value = ScreenState.Error()
                    getMLogger().logDebug("Error fetching mars photos: ${it.message}")
                }
                .onCompletion {
                    TopBarManager.updateState {
                        setIsVisible(true)
                        setTitle(date)
                    }
                }
                .launchIn(this)
        }
    }
}