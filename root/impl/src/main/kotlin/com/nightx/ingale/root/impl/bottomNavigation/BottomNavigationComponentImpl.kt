package com.nightx.ingale.root.impl.bottomNavigation

import com.arkivanov.essenty.backhandler.BackCallback
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.api.bottomBar.BottomNavigationState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

internal class BottomNavigationComponentImpl(
    appComponentContext: AppComponentContext,
    selectedTab: StateFlow<BottomBarItemViewState>,
    private val onTabSelectedCallback: (BottomBarItemViewState) -> Unit,
    private val bottomBarControllerImpl: BottomBarControllerImpl
) : BottomNavigationComponent, AppComponentContext by appComponentContext {

    private val backCallback = BackCallback {
        onTabSelectedCallback(BottomBarItemViewState.Local)
    }

    override val uiState: StateFlow<BottomNavigationState> = combine(
        selectedTab,
        bottomBarControllerImpl.isBottomBarVisible,
    ) { selectedTab, isBottomBarVisible ->
        BottomNavigationState(
            selectedTab = selectedTab,
            isVisible = isBottomBarVisible,
        )
    }.stateInWhileSubscribed(componentScope, BottomNavigationState())

    override fun onTabSelected(tab: BottomBarItemViewState) {
        if (bottomBarControllerImpl.isBottomBarVisible.value) {
            onTabSelectedCallback(tab)
        }
    }

    init {
        backHandler.register(backCallback)
    }
}