package com.hectordelgado.celestial.feature.solarflare

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SolarFlareScreenModel(
    private val nasaRepository: NasaRepository
) : StateScreenModel<SolarFlareState>(SolarFlareState.empty) {

    fun fetchSolarFlareData() {
        SnackbarManager.showMessage(
            SnackbarState(
                "Here is a message",
                "Here is the action",
                SnackbarDuration.Long
            ) {
                when (it) {
                    SnackbarResult.ActionPerformed -> {
                        getMLogger().logDebug("action performed")
                    }

                    SnackbarResult.Dismissed -> {
                        getMLogger().logDebug("action dismissed")
                    }
                }
            }
        )
//        screenModelScope.launch {
//            nasaRepository.fetchSolarFlareData(null, null)
//                .onStart {
//                    getMLogger().logDebug("Starting to fetch data")
//                    mutableState.value = SolarFlareState.empty
//                        .copy(headlinerTitle = "Fetching solar flare data...")
//                }
//                .onEach {
//                    getMLogger().logDebug("Found ${it.size} items")
//                    mutableState.value = SolarFlareState(
//                        headlinerTitle = "Found ${it.size} items",
//                        solarFlareSections = it.groupBy { it.beginTime }
//                    )
//                }
//                .catch {
//                    getMLogger().logDebug("There was an error: ${it.message}")
//                    //Log.d("logz", "There was an error: ${it.message}")
//                }
//                .launchIn(this)
//        }
    }
}

