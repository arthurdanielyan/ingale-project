package com.nightx.ingale.root.api.bottomBar

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import kotlinx.coroutines.flow.StateFlow

interface BottomNavigationComponent {

    val uiState: StateFlow<BottomNavigationState>

    val snackbarComponent: SnackbarComponent

    fun onTabSelected(tab: BottomBarItemViewState)

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext,
            selectedTab: StateFlow<BottomBarItemViewState>,
            onTabSelectedCallback: (BottomBarItemViewState) -> Unit,
        ): BottomNavigationComponent
    }
}