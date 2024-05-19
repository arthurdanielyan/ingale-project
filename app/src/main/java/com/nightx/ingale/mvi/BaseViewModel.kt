package com.nightx.ingale.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Effect : UiEffect> : ViewModel() {

    private val initialState: State by lazy { defineInitialState() }
    protected abstract fun defineInitialState(): State

    private val _state = MutableStateFlow(initialState)

    /**
     * Sometimes it is convenient to use a different approach to the ui state management.
     * For example having multiple [MutableStateFlow]s and combining them into a single one.
     * This is why this is an open property.
     * */
    open val state = _state.viewModelState()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    val currentState: State
        get() = _state.value

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