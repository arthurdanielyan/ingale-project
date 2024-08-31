package com.nightx.ingale.bottomBar.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.Lifecycle
import com.nightx.ingale.core.ui.OnLifecycleEvents
import kotlinx.coroutines.flow.Flow

interface BottomBarController {

    fun sendEffect(effect: BottomBarEffect)
}

interface BottomBarEffectsHolder {

    val bottomBarEffect: Flow<BottomBarEffect>
}

sealed interface BottomBarEffect {
    data object ShowBottomBar : BottomBarEffect
    data object HideBottomBar : BottomBarEffect
}

/**
 * Defines the BottomBar's state for the screen
 * */
@Composable
fun SetBottomBarState(
    effect: BottomBarEffect,
) {
    val bottomBarController = LocalBottomBarController.current
    OnLifecycleEvents { event ->
        if (event == Lifecycle.Event.ON_START) {
            bottomBarController.sendEffect(effect)
        }
    }
}

val LocalBottomBarEffects = staticCompositionLocalOf<BottomBarEffectsHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = staticCompositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}