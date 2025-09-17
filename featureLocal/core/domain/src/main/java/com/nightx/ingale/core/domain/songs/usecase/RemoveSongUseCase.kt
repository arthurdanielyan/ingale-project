package com.nightx.ingale.core.domain.songs.usecase

import com.nightx.ingale.core.domain.songs.repository.SongsCacheRepository

class RemoveSongUseCase(
    private val songsCacheRepository: SongsCacheRepository,
) {

    suspend operator fun invoke(id: Long) {
        songsCacheRepository.removeSong(id)
    }
}