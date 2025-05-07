package com.nightx.ingale.globalPlaybackPresentation.oldShit.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.presentation.viewModel.UiState

@Immutable
internal data class GlobalPlaybackViewState(
    val currentSongInfo: CurrentSongInfo = CurrentSongInfo.Empty,
    val isMusicBarExpanded: Boolean = true,
    val isMusicBarVisible: Boolean = false,
    val isPlaybackScreenVisible: Boolean = false,
) : UiState