package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<STATE, EFFECTS, EVENTS> : ViewModel() {
    abstract val state: StateFlow<STATE>
    abstract val effects: SharedFlow<EFFECTS>

    abstract fun onEvent(event: EVENTS)
}
