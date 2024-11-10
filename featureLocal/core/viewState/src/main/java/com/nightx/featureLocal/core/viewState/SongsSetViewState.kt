package com.nightx.featureLocal.core.viewState

import androidx.compose.runtime.Immutable
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.presentation.viewModel.UiState
import com.nightx.ingale.core.viewState.StableList
import com.nightx.ingale.core.viewState.emptyStableList

@Immutable
data class SongsSetViewState(
    val id: Long = -1,
    val title: String = "Loading...",
    val songs: StableList<SongViewState> = emptyStableList(),
    val iconPath: String = "",
) : UiState