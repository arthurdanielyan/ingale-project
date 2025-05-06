package com.nightx.ingale.root.impl.bottomNavigation

import com.nightx.ingale.bottomBarApi.BottomBarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BottomBarControllerImpl(
    private val applicationScope: CoroutineScope
) : BottomBarController {

    val isBottomBarVisible = MutableStateFlow(true)

    override fun setVisibility(isVisible: Boolean) {
        applicationScope.launch {
            isBottomBarVisible.update { isVisible }
        }
    }
}