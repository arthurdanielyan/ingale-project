package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel

import androidx.compose.runtime.Immutable
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.SongViewState

@Immutable
internal interface LocalMainCallbacks {

    fun onResume()
    fun onAlbumClick(songsSet: SongsSetViewState)
    fun onArtistClick(songsSet: SongsSetViewState)
    fun onSearchType(query: String)
    fun onSongClick(song: SongViewState)
    fun onPlaylistsClick()
    fun onFavouritesClick()
    fun onHistoryClick()
    fun refreshSongs()
    fun onGoToSettingsClick()
}