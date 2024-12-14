package com.nightx.ingale.globalPlaybackPresentation.view

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