package com.nightx.ingale.core.audio_player

import com.nightx.ingale.main_navigation.musicBar.CurrentSongInfo
import kotlinx.coroutines.flow.StateFlow

interface CurrentSongInfoStateHolder {

    val currentSongInfo: StateFlow<CurrentSongInfo>
}