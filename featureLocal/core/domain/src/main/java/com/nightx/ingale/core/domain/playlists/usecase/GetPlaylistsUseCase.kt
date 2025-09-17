package com.nightx.ingale.core.domain.playlists.usecase

import com.nightx.ingale.core.domain.playlists.model.Playlist
import com.nightx.ingale.core.domain.playlists.repository.PlaylistsRepository

class GetPlaylistsUseCase(
    private val repository: PlaylistsRepository
) {

    suspend operator fun invoke(): List<Playlist> {
        return repository.getPlaylists()
    }
}