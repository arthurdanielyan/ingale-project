package com.nightx.ingale.main_navigation.musicBar

import androidx.compose.runtime.Immutable

@Immutable
data class CurrentSongInfo( // moved
    val currentSongPreviewPath: String?,
    val isPlaying: Boolean,
    val songName: String?,
    val artistName: String?,
) {

    companion object {
        val Empty: CurrentSongInfo
            get() = CurrentSongInfo(
                currentSongPreviewPath = null,
                isPlaying = false,
                songName = null,
                artistName = null,
            )
    }
}