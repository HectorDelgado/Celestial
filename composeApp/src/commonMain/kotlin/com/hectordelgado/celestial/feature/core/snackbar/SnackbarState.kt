package com.hectordelgado.celestial.feature.core.snackbar

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult

data class SnackbarState(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onSnackbarResult: (SnackbarResult) -> Unit = {}
) {
    companion object {
        val empty = SnackbarState(
            message = "",
            actionLabel = "",
            duration = SnackbarDuration.Short,
            onSnackbarResult = {}
        )
    }
}