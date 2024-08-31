package com.nightx.ingale.core.audioPlayer.api

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domainModel.Song

@Immutable
interface PlayerUiActions {

    fun submitNewListAndPlay(songQueue: List<Song>, indexToPlay: Int)
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(@FloatRange(0.0, 1.0) progress: Float)
    fun changeFavoriteState()
}