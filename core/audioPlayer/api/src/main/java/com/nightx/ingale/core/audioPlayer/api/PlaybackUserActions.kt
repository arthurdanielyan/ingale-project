package com.nightx.ingale.core.audioPlayer.api

import com.nightx.ingale.core.domain.songs.model.Song

interface PlaybackUserActions {

    fun submitNewPlaylistAndPlay(playlist: List<Song>, indexToPlay: Int)
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(percentage: Float)
    fun changeFavoriteState()
    fun changePlaybackLoopMode(loopMode: PlaybackLoopMode)
}