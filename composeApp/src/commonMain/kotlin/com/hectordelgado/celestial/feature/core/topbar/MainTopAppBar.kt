package com.hectordelgado.celestial.feature.core.topbar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun MainTopAppBar(
    state: TopBarState,
    onLeftIconClick: (TopBarLeftIcon) -> Unit,
    onRightIconClick: (TopBarRightIcon) -> Unit
) {
    if (state.isVisible) {
        TopAppBar(
            title = {
                Text(text = state.title)
            },
            navigationIcon = {
                state.leftIcon?.let { icon ->
                    when (icon) {
                        TopBarLeftIcon.BACK -> {
                            IconButton(onClick = { onLeftIconClick(icon) }) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    "Back button"
                                )
                            }
                        }
                    }
                }
            },
            actions = {
                state.rightIcons
                    .takeIf { it.isNotEmpty() }
                    ?.forEach { icon ->
                        when(icon) {
                            TopBarRightIcon.OPTIONS -> {
                                IconButton(onClick = { onRightIconClick(icon) }) {
                                    Icon(
                                        Icons.Default.Menu,
                                        "Options button"
                                    )
                                }
                            }
                        }
                    }
            }
        )
    }
}