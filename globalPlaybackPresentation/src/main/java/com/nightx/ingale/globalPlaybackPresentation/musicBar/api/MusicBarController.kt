package com.nightx.ingale.globalPlaybackPresentation.musicBar.api

import androidx.compose.runtime.staticCompositionLocalOf

interface MusicBarController {

    fun sendEffect(effect: MusicBarEffect)
}

sealed interface MusicBarEffect {
    data object ExpandMusicBar : MusicBarEffect
    data object CollapseMusicBar : MusicBarEffect
}

val LocalMusicBarController = staticCompositionLocalOf<MusicBarController> {
    error("No BottomBarController provided")
}