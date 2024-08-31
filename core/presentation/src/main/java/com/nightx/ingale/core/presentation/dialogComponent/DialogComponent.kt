package com.nightx.ingale.core.presentation.dialogComponent

import androidx.compose.runtime.Stable
import com.nightx.ingale.core.presentation.viewModel.UiEffect
import com.nightx.ingale.core.presentation.viewModel.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
abstract class DialogComponent<State : UiState, Effect : UiEffect, VMCallback> {

    protected val componentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    internal val disposeEvent = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val initialState: State by lazy { defineInitialState() }
    protected abstract fun defineInitialState(): State

    // TODO: remove in the future (read the doc of [state])
    private val _state = MutableStateFlow(initialState)

    open val state = _state.dialogState()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    private val _vmCallbacks = Channel<VMCallback>()
    private val vmCallbacks = _vmCallbacks.receiveAsFlow()

    val currentState: State
        get() = state.value

    protected fun sendEffect(builder: () -> Effect) {
        componentScope.launch {
            _effect.send(builder())
        }
    }

    protected fun sendVmCallback(callback: VMCallback) {
        componentScope.launch {
            _vmCallbacks.send(callback)
        }
    }

    fun dismiss() {
        onDispose()
    }

    /**
     * Call from ViewModels to get events from the Dialog
     * */
    fun subscribeToVmCallbacks(callback: (VMCallback) -> Unit) {
        componentScope.launch {
            vmCallbacks.collect(callback)
        }
    }

    protected fun Flow<State>.dialogState() =
        this.stateIn(componentScope, SharingStarted.WhileSubscribed(), initialState)

    private fun onDispose() {
        disposeEvent.tryEmit(Unit)
        componentScope.cancel()
    }
}