package com.hectordelgado.celestial.feature.solarflare

import com.hectordelgado.celestial.network.dto.SolarFlareDto

data class SolarFlareState(
    val headlinerTitle: String,
    val solarFlareSections: Map<String, List<SolarFlareDto>>
) {
    companion object {
        val empty = SolarFlareState(headlinerTitle = "", solarFlareSections = emptyMap())
    }
}
