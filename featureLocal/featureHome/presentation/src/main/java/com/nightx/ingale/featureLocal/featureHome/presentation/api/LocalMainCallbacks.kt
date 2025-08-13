package com.nightx.ingale.featureLocal.featureHome.presentation.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState

@Immutable
interface LocalMainCallbacks {

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