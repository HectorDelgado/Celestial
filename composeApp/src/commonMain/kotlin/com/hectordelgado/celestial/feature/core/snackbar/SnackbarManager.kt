package com.hectordelgado.celestial.feature.core.snackbar

import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {
    private val _message: MutableStateFlow<SnackbarState?> = MutableStateFlow(null)
    val message: StateFlow<SnackbarState?> = _message.asStateFlow()

    fun showMessage(message: SnackbarState) {
        _message.value = message
    }

    fun showMessage(message: String) {
        showMessage(SnackbarState.empty.copy(message = message))
    }
}
