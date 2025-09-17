package com.nightx.ingale.featureLocal.featureHome.domain.usecases

import com.nightx.ingale.core.domain.songs.model.Song
import com.nightx.ingale.core.domain.songs.repository.SongsCacheRepository
import com.nightx.ingale.core.utils.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSongsUseCase(
    private val repository: SongsCacheRepository,
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