package com.nightx.ingale.core.domainModel.usecase

import com.nightx.ingale.core.domainModel.repository.SongsCacheRepository

class RemoveSongUseCase(
    private val songsCacheRepository: SongsCacheRepository,
) {

    suspend operator fun invoke(id: Long) {
        songsCacheRepository.removeSong(id)
    }
}