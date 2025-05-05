package com.nightx.ingale.root.impl.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.subscribe
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildContext
import com.nightx.ingale.core.decompose.appChildStack
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.root.RootScreenConfig
import com.nightx.ingale.root.api.root.toBottomBarItemViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class RootComponentImpl(
    appComponentContext: AppComponentContext,
    localComponentFactory: LocalComponent.Factory,
    bottomNavigationComponentFactory: BottomNavigationComponent.Factory,
) : RootComponent, AppComponentContext by appComponentContext {

    private val stackNavigation = StackNavigation<RootScreenConfig>()
    override val childStack = appChildStack(
        source = stackNavigation,
        serializer = RootScreenConfig.serializer(),
        initialConfiguration = RootScreenConfig.LocalConfig,
        handleBackButton = true,
        childFactory = { config, childComponentContext ->
            when (config) {
                is RootScreenConfig.LocalConfig -> {
                    localComponentFactory(
                        appComponentContext = childComponentContext
                    )
                }

                is RootScreenConfig.YoutubeConfig -> {
                    Unit
                }
            }
        },
        onNavigate = { config, onComplete ->
            stackNavigation.bringToFront(config) {
                onComplete(true)
            }
        },
        onPop = { onComplete ->
            onComplete(true)
        }
    )

    private val selectedTab = MutableStateFlow(BottomBarItemViewState.Local)

    override val bottomNavigationComponent = bottomNavigationComponentFactory(
        appComponentContext = appChildContext(
            key = "bottomNavigationComponent"
        ),
        selectedTab = selectedTab,
        onTabSelectedCallback = {
            stackNavigation.bringToFront(it.toConfig())
        }
    )

    init {
        childStack.subscribe(
            lifecycle = lifecycle,
        ) { childStack ->
            selectedTab.update {
                childStack.active.configuration.toBottomBarItemViewState()
            }
        }
    }

    private fun BottomBarItemViewState.toConfig(): RootScreenConfig {
        return when (this) {
            BottomBarItemViewState.Local -> RootScreenConfig.LocalConfig
            BottomBarItemViewState.Youtube -> RootScreenConfig.YoutubeConfig
        }
    }
}
