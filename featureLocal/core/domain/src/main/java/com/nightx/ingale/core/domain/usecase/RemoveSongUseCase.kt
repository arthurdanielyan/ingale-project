package com.nightx.ingale.core.domain.usecase

import com.nightx.ingale.core.domain.repository.SongsCacheRepository

class RemoveSongUseCase(
    private val songsCacheRepository: SongsCacheRepository,
) {

    suspend operator fun invoke(id: Long) {
        songsCacheRepository.removeSong(id)
    }
}