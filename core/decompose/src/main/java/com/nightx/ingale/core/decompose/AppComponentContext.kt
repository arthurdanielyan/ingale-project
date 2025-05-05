package com.nightx.ingale.core.decompose

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope

interface AppComponentContext : ComponentContext {

    val appRouter: AppRouter

    val componentScope: CoroutineScope
}