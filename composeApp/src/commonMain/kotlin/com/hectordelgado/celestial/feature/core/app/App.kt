package com.hectordelgado.celestial.feature.core.app

import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.feature.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            val topBarState by TopBarManager.state.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            SnackbarHostState()
            val snackbarState by SnackbarManager.message.collectAsState()

            MainAppContent(
                topBarState,
                snackbarHostState,
                snackbarState,
                navigator
            ) {
                CurrentScreen()
            }
        }
    }
}
