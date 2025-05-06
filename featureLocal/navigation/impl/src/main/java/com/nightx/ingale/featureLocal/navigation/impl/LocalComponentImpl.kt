package com.nightx.ingale.featureLocal.navigation.impl

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.lifecycle.doOnResume
import com.nightx.ingale.bottomBar.api.BottomBarController
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildStack
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalScreenConfig

internal class LocalComponentImpl(
    appComponentContext: AppComponentContext,
    private val bottomBarController: BottomBarController,
    localHomeComponentFactory: LocalHomeComponent.Factory,
    songsSetComponentFactory: SongsSetComponent.Factory,
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
                    songsSetComponentFactory(
                        appComponentContext = childComponentContext,
                        params = SongsSetComponent.Params(
                            id = config.id,
                            title = config.title,
                            songs = config.songs,
                            iconPath = config.iconPath
                        ),
                    )
            }
        },
        onNavigate = { config, onComplete ->
            stackNavigation.pushNew(config, onComplete)
        },
        onPop = { onComplete ->
            stackNavigation.pop(onComplete)
        }
    )

    init {
        doOnResume {
            controlBottomBarVisibility()
        }
    }

    private fun controlBottomBarVisibility() {
        childStack.subscribe(lifecycle) {
            bottomBarController.setVisibility(allowBottomBar(it.active.configuration))
        }
    }

    private fun allowBottomBar(config: LocalScreenConfig): Boolean {
        return when (config) {
            LocalScreenConfig.Home -> true
            is LocalScreenConfig.SongsSet -> false
        }
    }
}