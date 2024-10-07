package com.hectordelgado.celestial.feature.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreenModel : ScreenModel {
    fun onNavigationRequested(navigator: Navigator, screen: Screen) {
        navigator.push(screen)
    }
}