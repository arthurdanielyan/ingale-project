package com.nightx.ingale.core.audio_player.actions

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