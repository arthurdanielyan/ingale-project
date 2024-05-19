package com.nightx.ingale.feature_local.main.presentation.view.viewModel

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet

@Immutable
interface LocalMainCallbacks {

    fun onResume()
    fun onAlbumClick(songsSet: SongsSet)
    fun onArtistClick(songsSet: SongsSet)
    fun onSearchType(query: String)
    fun onSongClick(song: Song)
    fun onPlaylistsClick()
    fun onFavouritesClick()
    fun onHistoryClick()
    fun refreshSongs()
    fun onGoToSettingsClick()
}