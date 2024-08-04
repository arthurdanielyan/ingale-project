package com.nightx.ingale.core.presentation.dialogComponent

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val _isVisible = MutableStateFlow(false)
    val isVisible = _isVisible.asStateFlow()

    /**
     * Almost always it is more convenient to use a different approach for the ui
     * state management rather than using the [updateState] function. For example
     * having multiple [MutableStateFlow]s and combining ([Flow.combine]) them into a
     * single one. This is why this is an open property.
     * */
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

    protected fun updateState(modify: State.() -> State) {
        componentScope.launch {
            _state.update(modify)
        }
    }

    protected fun sendVmCallback(callback: VMCallback) {
        componentScope.launch {
            _vmCallbacks.send(callback)
        }
    }

    fun show() =
        _isVisible.update { true }

    fun dismiss() {
        _isVisible.update { false }
        onDispose()
    }

    fun subscribeToVmCallbacks(scope: CoroutineScope, callback: (VMCallback) -> Unit) {
        scope.launch {
            vmCallbacks.collect(callback)
        }
    }

    protected fun Flow<State>.dialogState() =
        this.stateIn(componentScope, SharingStarted.WhileSubscribed(), initialState)

    open fun onDispose() {
        disposeEvent.tryEmit(Unit)
        componentScope.cancel()
    }
}