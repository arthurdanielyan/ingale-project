package com.nightx.ingale.bottomBar.api

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.StateFlow

interface BottomBarController {

    fun setVisibility(isVisible: Boolean)
}

interface BottomBarStateHolder {

    val isBottomBarVisible: StateFlow<Boolean>
}

val LocalBottomBarState = staticCompositionLocalOf<BottomBarStateHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = staticCompositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}

/**
 * Provides information about whether the bottom bar tab this is accessed from
 * is the one visible to the user. When the bottom bar tab is not active the topmost screen in
 * it is still in RESUMED state.
 * */
val LocalBottomTabActivityState = compositionLocalOf<Boolean> {
    error("No BottomTabActivityState provided")
}
