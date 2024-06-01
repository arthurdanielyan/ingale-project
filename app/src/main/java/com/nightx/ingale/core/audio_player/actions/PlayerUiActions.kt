package com.nightx.ingale.core.audio_player.actions

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song

@Immutable
interface PlayerUiActions {

    fun submitNewListAndPlay(songQueue: List<Song>, indexToPlay: Int)
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(@FloatRange(0.0, 1.0) progress: Float)
    fun changeFavoriteState()
}