package com.nightx.ingale.featureLocal.featureHome.domain.usecases

import com.nightx.ingale.core.domainModel.model.Song
import com.nightx.ingale.core.utils.LoadState
import com.nightx.ingale.featureLocal.featureHome.domain.repository.LocalMainRepository
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