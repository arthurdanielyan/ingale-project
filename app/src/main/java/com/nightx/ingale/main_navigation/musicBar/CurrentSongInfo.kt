package com.nightx.ingale.main_navigation.musicBar

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

@Immutable
data class CurrentSongInfo(
    val currentSongThumbnail: Bitmap?,
    val isPlaying: Boolean,
    val songName: String?,
    val artistName: String?,
) {

    companion object {
        val Empty: CurrentSongInfo
            get() = CurrentSongInfo(
                currentSongThumbnail = null,
                isPlaying = false,
                songName = null,
                artistName = null,
            )
    }
}