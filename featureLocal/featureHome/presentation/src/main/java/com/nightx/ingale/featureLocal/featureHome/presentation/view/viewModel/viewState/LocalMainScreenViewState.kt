package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.viewState

import androidx.compose.runtime.Immutable
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.presentation.viewModel.UiState
import com.nightx.ingale.core.viewState.LoadingViewState
import com.nightx.ingale.core.viewState.StableList
import com.nightx.ingale.core.viewState.emptyStableList
import com.nightx.ingale.core.viewState.stableListOf
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.LocalMainViewModel.Companion.PERMISSION_NOT_GRANTED_ERROR
import com.nightx.ingale.resources.strings.R.string as Strings

@Immutable
internal data class LocalMainScreenViewState(
    val searchTextField: String = "",
    val songs: StableList<SongViewState> = emptyStableList(),
    val albums: StableList<SongsSetViewState> = emptyStableList(),
    val artists: StableList<SongsSetViewState> = emptyStableList(),
    val loadingState: LoadingViewState = LoadingViewState.Loading,
) : UiState {

    val sections: StableList<Int> = stableListOf(
        Strings.section_songs, Strings.section_albums, Strings.section_artist
    )

    val isPermissionError: Boolean
        get() {
            return loadingState is LoadingViewState.Error &&
                    loadingState.message == PERMISSION_NOT_GRANTED_ERROR
        }
}