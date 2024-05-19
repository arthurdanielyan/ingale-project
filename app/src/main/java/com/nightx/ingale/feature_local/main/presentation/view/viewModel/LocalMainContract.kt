package com.nightx.ingale.feature_local.main.presentation.view.viewModel

import androidx.compose.runtime.Immutable
import com.nightx.ingale.R
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.presentation.view.LoadingViewState
import com.nightx.ingale.feature_local.main.domain.model.Album
import com.nightx.ingale.feature_local.main.domain.model.Artist
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainViewModel.Companion.PERMISSION_NOT_GRANTED_ERROR
import com.nightx.ingale.mvi.UiEffect
import com.nightx.ingale.mvi.UiState
import com.nightx.ingale.mvi.wrappers.StableList
import com.nightx.ingale.mvi.wrappers.stableListOf

interface LocalMainContract {

    sealed interface Effect : UiEffect {
        data object ScrollToTop : Effect
    }

    @Immutable
    data class State(
        val searchTextField: String,
        val allSongs: StableList<Song>,
        val albums: StableList<Album>,
        val artists: StableList<Artist>,
        val songLoadingState: LoadingViewState,
    ) : UiState {

        val sections: StableList<Int> = stableListOf(
            R.string.section_songs, R.string.section_albums, R.string.section_artist
        )

        val isPermissionError: Boolean
            get() {
                return songLoadingState is LoadingViewState.Error &&
                        songLoadingState.message == PERMISSION_NOT_GRANTED_ERROR
            }
    }
}