package com.nightx.ingale.musicbar.musicBarControls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.ui.SingleLaunchedEffect
import kotlinx.coroutines.flow.Flow

interface MusicBarController {

    fun sendEffect(effect: MusicBarEffect)
}

interface MusicBarEffectsHolder {

    val musicBarEffect: Flow<MusicBarEffect>
}

sealed interface MusicBarEffect {
    data object ExpandMusicBar : MusicBarEffect
    data object CollapseMusicBar : MusicBarEffect
}

@Composable
fun SendMusicBarEffect(
    effect: MusicBarEffect
) {
    val bottomBarController = LocalMusicBarController.current
    SingleLaunchedEffect {
        bottomBarController.sendEffect(effect)
    }
}

val LocalMusicBarEffects = staticCompositionLocalOf<MusicBarEffectsHolder> {
    error("No BottomBarEffectsHolder provided")
}

val LocalMusicBarController = staticCompositionLocalOf<MusicBarController> {
    error("No BottomBarController provided")
}