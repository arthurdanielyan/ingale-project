package com.nightx.ingale.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {

    private val initialState: State by lazy { defineInitialState() }
    protected abstract fun defineInitialState(): State

    private val _state = MutableStateFlow(initialState)
    open val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialState)

    private val _event = MutableSharedFlow<Event>()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    val currentState: State
        get() = _state.value

    init {
        subscribeEvents()
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvent(it)
            }
        }
    }
    protected abstract fun handleEvent(event: Event)

    protected fun sendEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }

    protected fun updateState(modify: State.() -> State) {
        viewModelScope.launch {
            _state.emit(currentState.modify())
        }
    }

    protected fun Flow<State>.viewModelState() =
        this.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialState)
}