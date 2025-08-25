package com.nightx.ingale.root.impl.root

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.backhandler.BackCallback
import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildContext
import com.nightx.ingale.core.decompose.appChildSlot
import com.nightx.ingale.core.decompose.appChildStack
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.root.api.root.BottomBarItemViewState
import com.nightx.ingale.root.api.root.BottomNavigationState
import com.nightx.ingale.root.api.root.PlaybackScreenConfig
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.root.RootScreenConfig
import com.nightx.ingale.root.api.root.toBottomBarItemViewState
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

//@Stable
internal class RootComponentImpl(
    appComponentContext: AppComponentContext,
    localComponentFactory: LocalComponent.Factory,
    snackbarComponentFactory: SnackbarComponent.Factory,
    musicBarComponentFactory: MusicBarComponent.Factory,
    playbackScreenComponentFactory: PlaybackScreenComponent.Factory,
    private val bottomBarController: BottomBarController,
) : RootComponent, AppComponentContext by appComponentContext {

    private val backCallback = BackCallback {
        onTabSelected(BottomBarItemViewState.Local)
    }

    private val selectedTab = MutableStateFlow(BottomBarItemViewState.Local)

    override val uiState: StateFlow<BottomNavigationState> = combine(
        selectedTab,
        bottomBarController.isBottomBarVisible,
    ) { selectedTab, isBottomBarVisible ->
        BottomNavigationState(
            selectedTab = selectedTab,
            isVisible = isBottomBarVisible,
        )
    }.stateInWhileSubscribed(componentScope, BottomNavigationState())

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
            stackNavigation.pop {
                onComplete(it)
            }
        }
    )

    private val slotNavigation = SlotNavigation<PlaybackScreenConfig>()
    override val childSlot = appChildSlot(
        source = slotNavigation,
        serializer = PlaybackScreenConfig.serializer(),
        initialConfiguration = null,
        handleBackButton = true,
        childFactory = { _, childComponent ->
            playbackScreenComponentFactory(
                appComponentContext = childComponent,
                onClose = {
                    slotNavigation.dismiss()
                }
            )
        }
    )

    override val snackbarComponent = snackbarComponentFactory(
        appComponentContext = appChildContext("snackbarComponent"),
    )

    override val musicBarComponent = musicBarComponentFactory(
        appComponentContext = appChildContext("musicBarComponent"),
        onMusicBarClicked = {
            slotNavigation.activate(PlaybackScreenConfig)
        },
    )

    init {
        childStack.subscribe(
            lifecycle = lifecycle,
        ) { childStack ->
            selectedTab.update {
                childStack.active.configuration.toBottomBarItemViewState()
            }
        }
        backHandler.register(backCallback)
    }

    override fun onTabSelected(tab: BottomBarItemViewState) {
        if (bottomBarController.isBottomBarVisible.value) {
            stackNavigation.bringToFront(tab.toConfig())
        }
    }

    private fun BottomBarItemViewState.toConfig(): RootScreenConfig {
        return when (this) {
            BottomBarItemViewState.Local -> RootScreenConfig.LocalConfig
            BottomBarItemViewState.Youtube -> RootScreenConfig.YoutubeConfig
        }
    }
}
