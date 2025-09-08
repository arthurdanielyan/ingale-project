package com.nightx.ingale.core.presentation.presentationExt

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class UiEffectSender<UiEffect> {

    private val _effect = Channel<UiEffect>(
        capacity = Channel.BUFFERED
    )
    val uiEffect = _effect.receiveAsFlow()

    protected fun sendEffect(builder: () -> UiEffect) {
        _effect.trySend(builder())
    }
}