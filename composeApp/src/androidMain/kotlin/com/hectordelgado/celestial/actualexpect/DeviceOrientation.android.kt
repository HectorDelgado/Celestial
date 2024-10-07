package com.hectordelgado.celestial.actualexpect

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration


//actual class DeviceOrientation() {
//    @Composable
//    actual fun getCurrentOrientation(): OrientationType {
//        val orientation = context.resources.configuration.orientation
//
//        return when(orientation) {
//            Configuration.ORIENTATION_PORTRAIT -> OrientationType.PORTRAIT
//            Configuration.ORIENTATION_LANDSCAPE -> OrientationType.LANDSCAPE
//            else -> OrientationType.OTHER
//        }
//    }
//}

@Composable
actual fun getCurrentDeviceOrientation(): OrientationType {
    val configuration = LocalConfiguration.current

    return when(configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> OrientationType.PORTRAIT
        Configuration.ORIENTATION_LANDSCAPE -> OrientationType.LANDSCAPE
        else -> OrientationType.OTHER
    }
}
