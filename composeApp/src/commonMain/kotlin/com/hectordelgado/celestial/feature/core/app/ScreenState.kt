package com.hectordelgado.celestial.feature.core.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

sealed class ScreenState {
    data class Loading(
        val content: @Composable () -> Unit = { DefaultLoadingContent() }
    ) : ScreenState()
    data class Error(
        val errorMessage: String = "There was an error",
        val content: @Composable () -> Unit = { DefaultErrorContent(errorMessage) }
    ) : ScreenState()
    data object Success : ScreenState()
    data object Empty : ScreenState()
}

@Composable
fun ScreenContent(state: ScreenState, content: @Composable () -> Unit) {
    when (state) {
        is ScreenState.Loading -> state.content()
        is ScreenState.Error -> state.content()
        is ScreenState.Success -> content()
        is ScreenState.Empty -> {}
    }
}

@Composable
fun DefaultLoadingContent() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
        Text("Loading", fontSize = 24.sp, fontWeight = FontWeight.W400)
    }
}

@Composable
fun DefaultErrorContent(message: String = "There was an error") {
    Column(modifier = Modifier.fillMaxSize()) {
        Icon(Icons.Default.Warning, "")
        Text(message, fontSize = 24.sp, fontWeight = FontWeight.W400)
    }
}