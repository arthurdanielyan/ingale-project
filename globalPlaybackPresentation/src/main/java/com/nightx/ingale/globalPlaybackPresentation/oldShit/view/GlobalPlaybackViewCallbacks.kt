package com.nightx.ingale.globalPlaybackPresentation.oldShit.view

import androidx.compose.runtime.Immutable

@Immutable
interface GlobalPlaybackViewCallbacks {
    fun togglePlaybackScreenVisibility(isVisible: Boolean)
    fun onTogglePlaybackClick()
    fun onSkipToNextClick()
    fun onSkipToPreviousClick()
    fun onMusicBarClick()
    fun onClosePlaybackScreenClick()
}