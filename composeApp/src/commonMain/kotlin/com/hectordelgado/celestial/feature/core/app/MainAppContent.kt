package com.hectordelgado.celestial.feature.core.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.hectordelgado.celestial.feature.core.snackbar.SnackbarManager
import com.hectordelgado.celestial.feature.core.topbar.MainTopAppBar
import com.hectordelgado.celestial.feature.core.topbar.TopBarLeftIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarManager
import com.hectordelgado.celestial.feature.core.topbar.TopBarRightIcon
import com.hectordelgado.celestial.feature.core.topbar.TopBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun MainAppContent(
    topBarState: TopBarState,
    hostState: SnackbarHostState,
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    val snackbarState by SnackbarManager.message.collectAsState()

    LaunchedEffect(snackbarState) {
        snackbarState?.let {
            hostState.showSnackbar(
                message = it.message,
                actionLabel = it.actionLabel,
                duration = it.duration
            ).let(it.onSnackbarResult)
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
            SnackbarHost(hostState = hostState)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}


