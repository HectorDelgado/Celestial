package com.hectordelgado.celestial.feature.solarflare

import com.hectordelgado.celestial.network.response.SolarFlareDto

data class SolarFlareState(
    val headlinerTitle: String,
    val currentDate: String,
    val daysOffset: Long,
    val solarFlareSections: Map<String, List<SolarFlareDto>>
) {
    companion object {
        val empty = SolarFlareState(headlinerTitle = "", currentDate = "", daysOffset = 0L, solarFlareSections = emptyMap())
    }
}
