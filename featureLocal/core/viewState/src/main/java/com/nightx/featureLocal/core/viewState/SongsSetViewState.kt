package com.nightx.featureLocal.core.viewState

import androidx.compose.runtime.Immutable
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.presentation.viewModel.UiState
import com.nightx.ingale.core.viewState.StableList
import com.nightx.ingale.core.viewState.emptyStableList

@Immutable
data class SongsSetViewState(
    val id: Long,
    val title: String,
    val songs: StableList<SongViewState>,
    val iconPath: String = ""
): UiState {

    companion object {
        val Empty
            get() = SongsSetViewState(
                id = -1,
                title = "Loading...",
                songs = emptyStableList()
            )
    }
}