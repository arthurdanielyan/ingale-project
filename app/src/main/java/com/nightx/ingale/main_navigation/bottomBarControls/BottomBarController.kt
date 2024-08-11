package com.nightx.ingale.main_navigation.bottomBarControls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
    data object CollapseMusicBar : BottomBarEffect
    data object ExpandMusicBar : BottomBarEffect
}

@Composable
fun SendBottomBarEffect(
    effect: BottomBarEffect
) {
    val bottomBarController = LocalBottomBarController.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                bottomBarController.sendEffect(effect)
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

val LocalBottomBarEffects = staticCompositionLocalOf<BottomBarEffectsHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = staticCompositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}