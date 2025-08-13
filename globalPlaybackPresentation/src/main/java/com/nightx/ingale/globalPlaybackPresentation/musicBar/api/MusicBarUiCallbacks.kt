package com.nightx.ingale.globalPlaybackPresentation.musicBar.api

import androidx.compose.runtime.Immutable

@Immutable
interface MusicBarUiCallbacks {

    fun onTogglePlaybackClick()
    fun onSkipToNextClick()
    fun onMusicBarClick()
}