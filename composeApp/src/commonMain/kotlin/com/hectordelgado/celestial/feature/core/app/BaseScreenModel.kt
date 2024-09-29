package com.hectordelgado.celestial.feature.core.app

import cafe.adriel.voyager.core.model.StateScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseScreenModel<T>(
    initialState: T,
    initialScreenState: ScreenState
) : StateScreenModel<T>(initialState) {
    protected val mutableScreenState: MutableStateFlow<ScreenState> = MutableStateFlow(initialScreenState)
    public val screenState: StateFlow<ScreenState> = mutableScreenState.asStateFlow()
}