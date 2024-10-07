package com.hectordelgado.celestial.feature.core.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

sealed class ContentState {
    data class Loading(
        val content: @Composable () -> Unit = { DefaultLoadingContent() }
    ) : ContentState()

    data class Error(
        val content: @Composable () -> Unit = { DefaultErrorScreen() }
    ) : ContentState()

    data object Success : ContentState()

    data object Empty : ContentState()
}

/**
 * A base screen composable that handles the loading, error, and success states.
 */
@Composable
fun BaseScreen(state: ContentState, content: @Composable () -> Unit) {
    when (state) {
        is ContentState.Loading -> state.content()
        is ContentState.Error -> state.content()
        is ContentState.Success -> content()
        is ContentState.Empty -> {}
    }
}

@Composable
fun DefaultLoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text("Loading", fontSize = 24.sp, fontWeight = FontWeight.W400)
    }
}

@Composable
fun DefaultErrorScreen(message: String = "There was an error", modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Warning, "")
        Text(message, fontSize = 24.sp, fontWeight = FontWeight.W400)
    }
}