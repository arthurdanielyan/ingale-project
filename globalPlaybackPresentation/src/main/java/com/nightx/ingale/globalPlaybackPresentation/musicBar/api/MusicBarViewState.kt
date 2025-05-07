package com.nightx.ingale.globalPlaybackPresentation.musicBar.api

import androidx.compose.runtime.Immutable

@Immutable
data class MusicBarViewState(
    val currentSongPreviewPath: String? = null,
    val isPlaying: Boolean = false,
    val songName: String = "",
    val artistName: String = "",
    val isExpanded: Boolean = true,
    val isMusicBarVisible: Boolean = false,
)