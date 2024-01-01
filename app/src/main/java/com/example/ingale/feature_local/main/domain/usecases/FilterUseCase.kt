package com.example.ingale.feature_local.main.domain.usecases

import com.example.ingale.feature_local.main.domain.model.SongsSeparation
import com.example.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilterUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(query: String, songsSeparation: SongsSeparation): SongsSeparation =
        withContext(dispatcher) {
            SongsSeparation (
                songs = songsSeparation.songs.filter {
                    it.title.contains(query, true)
                }.toStableList(),
                albums = songsSeparation.albums.filter {
                    it.albumName.contains(query, true)
                }.toStableList(),
                artists = songsSeparation.artists.filter {
                    it.artistName.contains(query, true)
                }.toStableList()
            )
        }
}