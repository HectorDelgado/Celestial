package com.hectordelgado.celestial.feature.solarflare

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hectordelgado.celestial.actualexpect.getDateFormatter
import com.hectordelgado.celestial.actualexpect.getMLogger
import com.hectordelgado.celestial.data.NasaRepository
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SolarFlareScreenModel(
    private val nasaRepository: NasaRepository
) : StateScreenModel<SolarFlareState>(SolarFlareState.empty) {

    fun fetchSolarFlareData(daysOffset: Long = 0L) {
        screenModelScope.launch {
            val currentDate = getDateFormatter().getCurrentDateAsYYYYMMDD(daysOffset)

            nasaRepository.fetchSolarFlareData(currentDate, currentDate)
                .onStart {
                    mutableState.value = SolarFlareState.empty
                        .copy(
                            headlinerTitle = "Fetching solar flare data...",
                            daysOffset = daysOffset
                        )
                }
                .onEach { payload ->
                    mutableState.value = SolarFlareState(
                        headlinerTitle = if (payload.isEmpty()) "No solar flare data found for $currentDate" else "Solar flare data for $currentDate",
                        currentDate = currentDate,
                        daysOffset = daysOffset,
                        solarFlareSections = payload.map { solarFlareDto ->
                            solarFlareDto.copy(
                                beginTime = getDateFormatter().formatAsDayMonthYear(solarFlareDto.beginTime),
                                peakTime = getDateFormatter().formatAsDayMonthYear(solarFlareDto.peakTime),
                                endTime = solarFlareDto.endTime?.let { getDateFormatter().formatAsDayMonthYear(it) }
                                )
                            }
                            .groupBy { it.beginTime }
                    )
                }
                .catch {
                    getMLogger().logDebug("There was an error: ${it.message}")
                    mutableState.value = SolarFlareState.empty
                        .copy(
                            headlinerTitle = "0 Items found",
                            daysOffset = daysOffset
                        )
                    SnackbarManager.showMessage("There was an error.")
                }
                .launchIn(this)
        }
    }
}

