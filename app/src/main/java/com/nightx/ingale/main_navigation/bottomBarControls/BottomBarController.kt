package com.nightx.ingale.main_navigation.bottomBarControls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.presentation.view.SingleLaunchedEffect
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
    SingleLaunchedEffect {
        bottomBarController.sendEffect(effect)
    }
}

val LocalBottomBarEffects = staticCompositionLocalOf<BottomBarEffectsHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = staticCompositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}