package com.hectordelgado.celestial

import androidx.compose.ui.window.ComposeUIViewController
import com.hectordelgado.celestial.feature.core.app.App

fun MainViewController() = ComposeUIViewController { App() }