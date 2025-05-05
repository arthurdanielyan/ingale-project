package com.nightx.ingale.root.api.bottomBar

import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

interface BottomNavigationComponent {

    val uiState: StateFlow<BottomNavigationState>

    fun onTabSelected(tab: BottomBarItemViewState)

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext,
            selectedTab: StateFlow<BottomBarItemViewState>,
            onTabSelectedCallback: (BottomBarItemViewState) -> Unit,
        ): BottomNavigationComponent
    }
}