package com.nightx.ingale.root.impl.bottomNavigation

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import kotlinx.coroutines.flow.StateFlow

internal class BottomNavigationComponentFactory(
    private val snackbarComponentFactory: SnackbarComponent.Factory,
    private val musicBarComponentFactory: MusicBarComponent.Factory,
    private val bottomBarControllerImpl: BottomBarControllerImpl,
) : BottomNavigationComponent.Factory {
    override fun invoke(
        appComponentContext: AppComponentContext,
        selectedTab: StateFlow<BottomBarItemViewState>,
        onTabSelectedCallback: (BottomBarItemViewState) -> Unit,
    ): BottomNavigationComponent {
        return BottomNavigationComponentImpl(
            appComponentContext = appComponentContext,
            selectedTab = selectedTab,
            snackbarComponentFactory = snackbarComponentFactory,
            musicBarComponentFactory = musicBarComponentFactory,
            onTabSelectedCallback = onTabSelectedCallback,
            bottomBarControllerImpl = bottomBarControllerImpl,
        )
    }
}