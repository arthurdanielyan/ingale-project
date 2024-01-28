package com.example.ingale.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn

/**
 * Sometimes certain effects emitted form ViewModels need to be handled by
 * child composables. This function filters those effects and returns the flow
 * containing only them
 * */
@Composable
inline fun <reified E: UiEffect> Flow<*>.rememberFlowOf() = remember {
    this.filterIsInstance<E>()
}

/**
 * Transform this Channel into a hot flow.
 * In contrast to [receiveAsFlow], elements emitted by this flow
 * will be received by all collectors.
 * */
fun <T> Channel<T>.asFlow(scope: CoroutineScope): Flow<T> = flow {
    for(effect in this@asFlow) {
        emit(effect)
    }
}.shareIn(scope, SharingStarted.Eagerly, Int.MAX_VALUE)
