package com.nightx.ingale.main_navigation.bottom_bar_controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
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
    data object CollapseMusicInfo : BottomBarEffect
    data object ExpandMusicInfo : BottomBarEffect
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

val LocalBottomBarEffects = compositionLocalOf<BottomBarEffectsHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalBottomBarController = compositionLocalOf<BottomBarController> {
    error("No BottomBarController provided")
}