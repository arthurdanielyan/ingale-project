package com.nightx.ingale.root.impl.bottomNavigation

import com.nightx.ingale.bottomBar.api.BottomBarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BottomBarControllerImpl(
    private val scope: CoroutineScope
) : BottomBarController {

    val isBottomBarVisible = MutableStateFlow(true)

    override fun setVisibility(isVisible: Boolean) {
        scope.launch {
            isBottomBarVisible.update { isVisible }
        }
    }
}