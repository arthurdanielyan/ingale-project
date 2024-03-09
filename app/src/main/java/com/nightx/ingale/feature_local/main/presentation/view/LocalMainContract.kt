package com.nightx.ingale.feature_local.main.presentation.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.presentation.view.LoadingViewState
import com.nightx.ingale.feature_local.main.domain.model.Album
import com.nightx.ingale.feature_local.main.domain.model.Artist
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_ALBUMS
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_ARTISTS
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_SONGS
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
        val songLoadingState: LoadingViewState
    ): UiState {

        val sections: StableList<String> = stableListOf(SECTION_SONGS, SECTION_ALBUMS, SECTION_ARTISTS)
    }
}