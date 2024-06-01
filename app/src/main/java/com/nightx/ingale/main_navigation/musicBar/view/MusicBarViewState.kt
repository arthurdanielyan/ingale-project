package com.nightx.ingale.main_navigation.musicBar.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.main_navigation.musicBar.CurrentSongInfo
import com.nightx.ingale.mvi.UiState

@Immutable
data class MusicBarViewState(
    val currentSongInfo: CurrentSongInfo = CurrentSongInfo.Empty,
    val isMusicDetailsExpanded: Boolean = true,
    val isMusicBarVisible: Boolean = false
) : UiState