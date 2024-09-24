package com.hectordelgado.celestial.feature.core.app

import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.feature.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(SplashScreen()) { navigator ->
            val topBarState by TopBarManager.state.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            MainAppContent(topBarState, snackbarHostState, navigator) {
                CurrentScreen()
            }
        }
    }
}
