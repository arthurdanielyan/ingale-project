package com.nightx.ingale.core.audioPlayer.api

import com.nightx.ingale.core.domainModel.Song

interface PlaybackUserActions {

    fun submitNewListAndPlay(songQueue: List<Song>, indexToPlay: Int)
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(percentage: Float)
    fun changeFavoriteState()
}