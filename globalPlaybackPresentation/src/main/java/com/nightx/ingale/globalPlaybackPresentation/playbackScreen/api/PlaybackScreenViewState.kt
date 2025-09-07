package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.audioPlayer.api.PlaybackLoopMode

@Immutable
data class PlaybackScreenViewState(
    val currentSongPreviewPath: String? = "",
    val isPlaying: Boolean = false,
    val songName: String = "",
    val artistName: String = "",
    val loopMode: PlaybackLoopMode = PlaybackLoopMode.PlaylistLoop,
)
