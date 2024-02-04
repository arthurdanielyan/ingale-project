package com.night.ingale.feature_local.main.domain.usecases

import com.night.ingale.core.domain.model.Song
import com.night.ingale.feature_local.main.domain.repository.LocalMainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetSongsUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val repository: LocalMainRepository
) {

    suspend operator fun invoke(): List<Song> =
        withContext(dispatcher) {
            repository.getSongs().filter {
                it.duration > 20_000
            }.sortedByDescending { it.lastModified }
        }
}