package com.nightx.ingale.core.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.eventFlow
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import kotlinx.coroutines.CoroutineScope

@Composable
fun SingleLaunchedEffect(
    block: suspend CoroutineScope.() -> Unit
) = LaunchedEffect(Unit, block)

@Composable
fun OnLifecycleEvents(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    block: (Lifecycle.Event) -> Unit
) {
    ComposeCollect(
        flow = lifecycleOwner.lifecycle.eventFlow,
        onEvent = block
    )
}

/**
 * Called when [key] changes. In contrast to [LaunchedEffect] this
 * doesn't re-execute [block] when configuration change happens. Note that [T]
 * has to be saveable
 * */
@Composable
fun <T> ObserveState(
    key: T,
    saver: Saver<T, out Any> = autoSaver(),
    block: suspend CoroutineScope.(T) -> Unit,
) {
    var lastValue by rememberSaveable(stateSaver = saver) {
        mutableStateOf(key)
    }
    LaunchedEffect(key) {
        if(lastValue != key) {
            block(key)
        }
        lastValue = key
    }
}

@Composable
fun <T1> LaunchedEffect(
    key1: T1,
    block: suspend CoroutineScope.(p1: T1) -> Unit
) {
    LaunchedEffect(
        key1 = key1
    ) {
        block(key1)
    }
}

@Composable
fun <T1, T2> LaunchedEffect(
    key1: T1,
    key2: T2,
    block: suspend CoroutineScope.(p1: T1, p2: T2) -> Unit
) {
    LaunchedEffect(
        key1 = key1,
        key2 = key2
    ) {
        block(key1, key2)
    }
}

@Composable
fun <T1, T2, T3> LaunchedEffect(
    key1: T1,
    key2: T2,
    key3: T3,
    block: suspend CoroutineScope.(p1: T1, p2: T2, p3: T3) -> Unit
) {
    LaunchedEffect(
        key1 = key1,
        key2 = key2,
        key3 = key3
    ) {
        block(key1, key2, key3)
    }
}

@Composable
fun <T1, T2, T3, T4> LaunchedEffect(
    key1: T1,
    key2: T2,
    key3: T3,
    key4: T4,
    block: suspend CoroutineScope.(p1: T1, p2: T2, p3: T3, p4: T4) -> Unit
) {
    LaunchedEffect(
        key1,
        key2,
        key3,
        key4
    ) {
        block(key1, key2, key3, key4)
    }
}

@Composable
fun <T1, T2, T3, T4, T5> LaunchedEffect(
    key1: T1,
    key2: T2,
    key3: T3,
    key4: T4,
    key5: T5,
    block: suspend CoroutineScope.(
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5
    ) -> Unit
) {
    LaunchedEffect(
        key1,
        key2,
        key3,
        key4,
        key5
    ) {
        block(key1, key2, key3, key4, key5)
    }
}