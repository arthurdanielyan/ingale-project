package com.nightx.ingale.core.domain.playlists.repository

import com.nightx.ingale.core.domain.playlists.model.Playlist

interface PlaylistsRepository {

    suspend fun getPlaylists(): List<Playlist>

    suspend fun createNewPlaylist(name: String, initialSongId: Long? = null): Result<Unit>

    suspend fun addSongToPlaylist(songId: Long, playlistName: String)
}