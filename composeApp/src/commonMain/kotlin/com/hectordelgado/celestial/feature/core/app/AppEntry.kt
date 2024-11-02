package com.hectordelgado.celestial.feature.core.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarState
import com.hectordelgado.celestial.feature.core.topbar.MainTopAppBar
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.feature.core.topbar.TopBarRightIcon
import com.hectordelgado.celestial.feature.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main entry point into the application.
 */
@Composable
@Preview
fun AppEntry() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            val topBarState by TopBarManager.state.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarState by SnackbarManager.message.collectAsState()

            LaunchedEffect(snackbarState) {
                if (snackbarState != SnackbarState.empty) {
                    snackbarHostState.showSnackbar(
                        message = snackbarState.message,
                        actionLabel = snackbarState.actionLabel,
                        duration = snackbarState.duration
                    ).let(snackbarState.onSnackbarResult)
                }
            }

            Scaffold(
                topBar = {
                    MainTopAppBar(
                        state = topBarState,
                        onLeftIconClick = {
                            when (it) {
                                TopBarLeftIcon.BACK -> {
                                    navigator.pop()
                                }
                                TopBarLeftIcon.CLOSE -> {
                                    navigator.pop()
                                }
                            }
                        },
                        onRightIconClick = {
                            when (it) {
                                TopBarRightIcon.OPTIONS -> {
                                    // todo: show options
                                }
                            }
                        },
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CurrentScreen()
                }
            }
        }
    }
}
