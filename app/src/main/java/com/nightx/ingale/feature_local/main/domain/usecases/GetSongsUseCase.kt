package com.nightx.ingale.feature_local.main.domain.usecases

import com.nightx.ingale.core.domain.LoadState
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.main.domain.repository.LocalMainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSongsUseCase(
    private val repository: LocalMainRepository,
) {

    suspend operator fun invoke(): Flow<LoadState<List<Song>>> =
        repository.getSongs().map { songsLoadState ->
            if (songsLoadState is LoadState.Success) {
                LoadState.Success(
                    songsLoadState.data.filter { it.duration > 20_000 }
                        .sortedByDescending { it.lastModified }
                )
            } else songsLoadState
        }
}