package com.hectordelgado.celestial.feature.splash

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenModel : StateScreenModel<SplashState>(SplashState.empty) {
    fun restoreState() {
        screenModelScope.launch {
            // simulate loading
            delay(1500)
            mutableState.value = mutableState.value.copy(isLoading = false)
        }
    }

    fun onNavigationRequested(navigator: Navigator, screen: Screen) {
        navigator.push(screen)
    }
}