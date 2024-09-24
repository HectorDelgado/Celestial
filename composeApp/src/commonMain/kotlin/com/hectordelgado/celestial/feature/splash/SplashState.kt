package com.hectordelgado.celestial.feature.splash

data class SplashState(val isLoading: Boolean) {
    companion object {
        val empty = SplashState(isLoading = true)
    }
}
