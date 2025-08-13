package com.nightx.ingale.core.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * This handles coroutine scope, doesn't provide any navigation logic
 */
class DefaultAppComponentContext(
    componentContext: ComponentContext,
) : AppComponentContext, ComponentContext by componentContext {

    override val appRouter = AppRouterImpl()

    override val componentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        doOnDestroy {
            componentScope.cancel()
        }
    }
}