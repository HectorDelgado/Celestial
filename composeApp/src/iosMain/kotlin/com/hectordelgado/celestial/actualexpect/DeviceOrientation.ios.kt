package com.hectordelgado.celestial.actualexpect

import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation

@Composable
actual fun getCurrentDeviceOrientation(): OrientationType {
    val orientation = UIDevice.currentDevice.orientation

    return when(orientation) {
        UIDeviceOrientation.UIDeviceOrientationPortrait,
        UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> OrientationType.PORTRAIT
        UIDeviceOrientation.UIDeviceOrientationLandscapeLeft,
        UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> OrientationType.LANDSCAPE
        else -> OrientationType.OTHER
    }
}

