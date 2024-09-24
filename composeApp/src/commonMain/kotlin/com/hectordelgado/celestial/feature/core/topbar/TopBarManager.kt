package com.hectordelgado.celestial.feature.core.topbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TopBarManager {
    private val _state: MutableStateFlow<TopBarState> = MutableStateFlow(TopBarState.empty)
    val state: StateFlow<TopBarState> = _state.asStateFlow()

    fun resetStateTo(block: TopBarState.Builder.() -> Unit) {
        val state = TopBarState.Builder(TopBarState.empty)
            .apply(block)
            .build()
        _state.value = state
    }

    fun updateState(block: TopBarState.Builder.() -> Unit) {
        val state = TopBarState.Builder(_state.value)
            .apply {
                block()
            }
            .build()

        _state.value = state
    }
}