package com.nightx.ingale.bottomBar.impl.bottomBar

import com.nightx.ingale.bottomBar.api.BottomBarController
import com.nightx.ingale.bottomBar.api.BottomBarStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BottomBarControllerImpl : BottomBarController, BottomBarStateHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _bottomBarState = MutableStateFlow(true)
    override val isBottomBarVisible = _bottomBarState

    override fun setVisibility(isVisible: Boolean) {
        scope.launch {
            _bottomBarState.update { isVisible }
        }
    }
}