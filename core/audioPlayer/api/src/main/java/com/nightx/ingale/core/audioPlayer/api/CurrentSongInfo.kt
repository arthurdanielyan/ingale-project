package com.nightx.ingale.core.audioPlayer.api


data class CurrentSongInfo(
    val currentSongPreviewPath: String?,
    val isPlaying: Boolean,
    val songName: String,
    val artistName: String,
    val seekPercentage: Float,
) {

    companion object {
        val Empty: CurrentSongInfo
            get() = CurrentSongInfo(
                currentSongPreviewPath = null,
                isPlaying = false,
                songName = "",
                artistName = "",
                seekPercentage = 0f,
            )
    }
}