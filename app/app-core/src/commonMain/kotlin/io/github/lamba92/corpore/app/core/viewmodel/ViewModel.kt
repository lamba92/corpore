package io.github.lamba92.corpore.app.core.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MVIViewModel<STATE, EFFECTS, EVENTS> {
    val state: StateFlow<STATE>
    val effects: SharedFlow<EFFECTS>

    fun onEvent(event: EVENTS)
}