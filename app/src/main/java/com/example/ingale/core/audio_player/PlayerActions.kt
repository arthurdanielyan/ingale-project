package com.example.ingale.core.audio_player

import androidx.annotation.FloatRange

interface PlayerActions {

    fun togglePlaying()

    fun skipToNext()

    fun skipToPrevious()

    fun seekTo(@FloatRange(0.0, 1.0) progress: Float)
}