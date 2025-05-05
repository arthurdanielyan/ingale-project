package com.nightx.ingale.root.api.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent

interface RootComponent {

    val childStack: Value<ChildStack<RootScreenConfig, Any>>
    val bottomNavigationComponent: BottomNavigationComponent

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext,
        ): RootComponent
    }
}