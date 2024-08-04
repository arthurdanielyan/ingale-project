package com.nightx.ingale.core.audioPlayer.api

import kotlinx.coroutines.flow.StateFlow

interface CurrentSongInfoStateProvider {

    val currentSongInfo: StateFlow<CurrentSongInfo>
}