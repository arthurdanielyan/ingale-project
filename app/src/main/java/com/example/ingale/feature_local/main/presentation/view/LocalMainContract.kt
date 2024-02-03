package com.example.ingale.feature_local.main.presentation.view

import androidx.compose.runtime.Immutable
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.feature_local.main.domain.model.Album
import com.example.ingale.feature_local.main.domain.model.Artist
import com.example.ingale.core.domain.model.Song
import com.example.ingale.core.presentation.view.LoadingViewState
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_ALBUMS
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_ARTISTS
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.SECTION_SONGS
import com.example.ingale.mvi.UiEffect
import com.example.ingale.mvi.UiEvent
import com.example.ingale.mvi.UiState
import com.example.ingale.mvi.wrappers.StableList
import com.example.ingale.mvi.wrappers.stableListOf

interface LocalMainContract {

    sealed interface Event : UiEvent {
        data object Refresh : Event
        class AlbumClicked(val songsSet: SongsSet) : Event
        class ArtistClicked(val songsSet: SongsSet) : Event
        class Search(val query: String) : Event
        class PlaySong(val song: Song) : Event
    }

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