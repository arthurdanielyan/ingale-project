package com.nightx.ingale.core.audioPlayer.api

import kotlinx.coroutines.flow.StateFlow

interface CurrentSongInfoStateProvider {

    val currentPlaybackInfo: StateFlow<CurrentPlaybackInfo?>
}