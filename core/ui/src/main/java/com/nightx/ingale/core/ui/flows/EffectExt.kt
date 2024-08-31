package com.nightx.ingale.core.ui.flows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

/**
 * Sometimes certain effects emitted form ViewModels need to be handled by
 * child composables. This function filters those effects and returns the flow
 * containing only them
 * */
@Composable
inline fun <reified E> Flow<*>.rememberFlowOf() = remember {
    this.filterIsInstance<E>()
}