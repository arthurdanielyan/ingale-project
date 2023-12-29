package com.example.ingale.mvi

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun <E: UiEffect> Flow<*>.rememberFlowOf(klass: KClass<E>) = remember {
    this.filterIsInstance(klass)
}

/**
 * Transform this Channel into a hot flow.
 * In contrast to [ReceiveChannel.receiveAsFlow], elements will be emitted to all collectors
 * */
fun <E: UiEffect> Channel<E>.asFlow(scope: CoroutineScope): Flow<E> = flow {
    for(effect in this@asFlow) {
        Log.d("myLogs", "Emitting")
        emit(effect)
    }
}.shareIn(scope, SharingStarted.Eagerly, Int.MAX_VALUE)
