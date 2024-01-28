package com.example.ingale.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun SingleLaunchedEffect(
    block: suspend CoroutineScope.() -> Unit
) = LaunchedEffect(Unit, block)