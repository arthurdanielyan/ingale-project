package com.nightx.ingale.bottomBar.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.ui.SingleLaunchedEffect
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