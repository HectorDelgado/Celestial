package com.hectordelgado.celestial.actualexpect

import androidx.compose.runtime.Composable


enum class OrientationType {
    PORTRAIT,
    LANDSCAPE,
    OTHER
}

//expect class DeviceOrientation {
//    @Composable
//    fun getCurrentOrientation(): OrientationType
//}

@Composable
expect fun getCurrentDeviceOrientation(): OrientationType
