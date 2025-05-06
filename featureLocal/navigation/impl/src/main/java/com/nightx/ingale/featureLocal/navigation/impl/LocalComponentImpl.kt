package com.nightx.ingale.featureLocal.navigation.impl

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildStack
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalScreenConfig

internal class LocalComponentImpl(
    appComponentContext: AppComponentContext,
    localHomeComponentFactory: LocalHomeComponent.Factory
) : LocalComponent, AppComponentContext by appComponentContext {

    private val stackNavigation = StackNavigation<LocalScreenConfig>()
    override val childStack = appChildStack(
        source = stackNavigation,
        serializer = LocalScreenConfig.serializer(),
        initialConfiguration = LocalScreenConfig.Home,
        handleBackButton = true,
        childFactory = { config, childComponentContext ->
            when (config) {
                LocalScreenConfig.Home ->
                    localHomeComponentFactory(
                        appComponentContext = childComponentContext
                    )

                is LocalScreenConfig.SongsSet ->
                    Unit
            }
        },
        onNavigate = { config, onComplete ->
            stackNavigation.pushNew(config, onComplete)
        },
        onPop = { onComplete ->
            stackNavigation.pop(onComplete)
        }
    )
}