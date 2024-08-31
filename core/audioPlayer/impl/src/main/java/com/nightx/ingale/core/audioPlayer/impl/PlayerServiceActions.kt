package com.nightx.ingale.core.audioPlayer.impl

import androidx.annotation.FloatRange

interface PlayerServiceActions {

    fun initService()
    fun stopService()
    fun togglePlaying()
    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(@FloatRange(0.0, 1.0) progress: Float)
    fun changeFavoriteState()
}