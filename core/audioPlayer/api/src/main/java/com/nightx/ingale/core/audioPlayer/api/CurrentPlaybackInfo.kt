package com.nightx.ingale.core.audioPlayer.api


data class CurrentPlaybackInfo(
    val currentSongPreviewPath: String?,
    val isPlaying: Boolean,
    val songName: String,
    val artistName: String,
    val seekPercentage: Float,
    val loopMode: PlaybackLoopMode,
)