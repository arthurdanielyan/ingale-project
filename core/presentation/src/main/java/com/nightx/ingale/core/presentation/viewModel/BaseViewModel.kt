package com.nightx.ingale.core.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Effect : UiEffect> : ViewModel() {

    private val initialState: State by lazy { defineInitialState() }
    protected abstract fun defineInitialState(): State

    // TODO: remove in the future (read the doc of [state])
    private val _state = MutableStateFlow(initialState)

    /**
     * Almost always it is more convenient to use a different approach for the ui
     * state management rather than using the [updateState] function. For example
     * having multiple [MutableStateFlow]s and combining ([Flow.combine]) them into a
     * single one. This is why this is an open property.
     * */
    open val state = _state.viewModelState()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    val currentState: State
        get() = state.value

    protected fun sendEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }

    protected fun updateState(modify: State.() -> State) {
        viewModelScope.launch {
            _state.update(modify)
        }
    }

    protected fun Flow<State>.viewModelState() =
        this.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialState)
}