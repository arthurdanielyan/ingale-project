package com.nightx.ingale.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest

/**
 * Transform this Channel into a hot flow.
 * In contrast to [receiveAsFlow], elements emitted by this flow
 * will be received by all collectors.
 * */
fun <T> Channel<T>.asFlow(scope: CoroutineScope): Flow<T> = flow {
    for (effect in this@asFlow) {
        emit(effect)
    }
}.shareIn(scope, SharingStarted.Eagerly, Int.MAX_VALUE)


@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.onEachLatest(action: suspend (T) -> Unit): Flow<T> =
    transformLatest { value ->
        action(value)
        return@transformLatest emit(value)
    }