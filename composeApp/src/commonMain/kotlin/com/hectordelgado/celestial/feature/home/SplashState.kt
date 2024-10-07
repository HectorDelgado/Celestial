package com.hectordelgado.celestial.feature.home

data class SplashState(val isLoading: Boolean) {
    companion object {
        val empty = SplashState(isLoading = true)
    }
}
