package com.nightx.ingale.musicbar.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.presentation.viewModel.UiState

@Immutable
internal data class MusicBarViewState(
    val currentSongInfo: CurrentSongInfo = CurrentSongInfo.Empty,
    val isMusicDetailsExpanded: Boolean = true,
    val isMusicBarVisible: Boolean = false
) : UiState