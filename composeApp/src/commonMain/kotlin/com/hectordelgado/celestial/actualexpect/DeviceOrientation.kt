package com.hectordelgado.celestial.actualexpect

import androidx.compose.runtime.Composable


enum class OrientationType {
    PORTRAIT,
    LANDSCAPE,
    OTHER
}

@Composable
expect fun getCurrentDeviceOrientation(): OrientationType
