package com.night.ingale.core.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun SingleLaunchedEffect(
    block: suspend CoroutineScope.() -> Unit
) = LaunchedEffect(Unit, block)

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