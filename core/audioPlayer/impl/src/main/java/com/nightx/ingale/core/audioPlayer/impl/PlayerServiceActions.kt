package com.nightx.ingale.core.audioPlayer.impl

import androidx.annotation.FloatRange

interface PlayerServiceActions {

    fun prepareNewSong()
    fun stopService()
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(@FloatRange(from = 0.0, to = 1.0) progress: Float)
    fun changePlaybackLoopMode()
}