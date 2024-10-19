package com.nightx.ingale.bottomBar.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.Lifecycle
import com.nightx.ingale.core.ui.OnLifecycleEvents
import kotlinx.coroutines.flow.StateFlow

interface BottomBarController {

    fun setVisibility(isVisible: Boolean)
}

interface BottomBarStateHolder {

    val isBottomBarVisible: StateFlow<Boolean>
}

/**
 * Defines the BottomBar's state for the screen
 * */
@Composable
fun SetBottomBarVisibility(
    isVisible: Boolean,
) {
    val bottomBarController = LocalBottomBarController.current

    OnLifecycleEvents { event ->
        if (event == Lifecycle.Event.ON_START) {
            bottomBarController.setVisibility(isVisible)
        }
    }
}

val LocalBottomBarState = staticCompositionLocalOf<BottomBarStateHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = staticCompositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}