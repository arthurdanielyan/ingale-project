package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api

import androidx.compose.runtime.Immutable

@Immutable
data class PlaybackScreenViewState(
    val currentSongPreviewPath: String? = "",
    val isPlaying: Boolean = false,
    val songName: String = "",
    val artistName: String = "",
    val seekPercentage: Float = 0f,
)
