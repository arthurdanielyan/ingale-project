package com.nightx.ingale.core.domain.playlists.usecase

import com.nightx.ingale.core.domain.playlists.repository.PlaylistsRepository

class AddSongToPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {

    suspend operator fun invoke(playlistName: String, songId: Long) {
        playlistsRepository.addSongToPlaylist(
            playlistName = playlistName,
            songId = songId,
        )
    }
}