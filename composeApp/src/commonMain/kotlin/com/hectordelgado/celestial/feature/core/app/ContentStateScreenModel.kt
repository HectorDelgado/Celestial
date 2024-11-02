package com.hectordelgado.celestial.feature.core.app

import cafe.adriel.voyager.core.model.StateScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Wrapper around [StateScreenModel] that adds flow of [ContentState]
 */
abstract class ContentStateScreenModel<S>(
    initialState: S,
    initialContentState: ContentState
) : StateScreenModel<S>(initialState) {
    protected val mutableContentState: MutableStateFlow<ContentState> = MutableStateFlow(initialContentState)
    val contentState: StateFlow<ContentState> = mutableContentState.asStateFlow()
}
