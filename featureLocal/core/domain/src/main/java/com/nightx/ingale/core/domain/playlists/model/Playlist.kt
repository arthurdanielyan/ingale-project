package com.nightx.ingale.core.domain.playlists.model

import com.nightx.ingale.core.domain.songs.model.Song

data class Playlist(
    val name: String,
    val songs: List<Song>
)