package com.nightx.ingale.root.impl.bottomNavigation

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import kotlinx.coroutines.flow.StateFlow

internal class BottomNavigationComponentFactory(
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
            onTabSelectedCallback = onTabSelectedCallback,
            bottomBarControllerImpl = bottomBarControllerImpl,
        )
    }
}