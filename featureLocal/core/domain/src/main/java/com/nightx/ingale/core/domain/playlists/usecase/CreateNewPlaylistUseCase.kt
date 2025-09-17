package com.nightx.ingale.core.domain.playlists.usecase

import com.nightx.ingale.core.domain.playlists.repository.PlaylistsRepository

class CreateNewPlaylistUseCase(
    private val repository: PlaylistsRepository,
) {

    suspend operator fun invoke(name: String, initialSongId: Long? = null): Result<Unit> {
        return repository.createNewPlaylist(
            name = name,
            initialSongId = initialSongId,
        )
    }
}