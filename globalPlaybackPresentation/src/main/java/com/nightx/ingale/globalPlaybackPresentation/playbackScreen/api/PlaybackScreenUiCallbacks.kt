package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable

@Immutable
interface PlaybackScreenUiCallbacks {

    fun onTogglePlayback()
    fun onSkipToNextClick()
    fun onSkipToPreviousClick()
    fun onSeekTo(
        @FloatRange(from = 0.0, to = 1.0) percentage: Float
    )

    fun onClose()
}