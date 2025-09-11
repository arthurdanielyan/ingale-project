package com.nightx.ingale.featureLocal.featureHome.domain.usecases

import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.utils.CoroutineDispatchers
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSeparation
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSet
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSetType
import kotlinx.coroutines.withContext

class OrganizeSongsUseCase(
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(songs: List<Song>): SongsSeparation =
        withContext(dispatchers.default) {
            SongsSeparation(
                songs = songs,
                albums = songs.let {
                    val albums = mutableListOf<SongsSet>()
                    it.forEach { song ->
                        val albumIndex = albums.indexOfFirst { it.id == song.albumId }
                        if (albumIndex >= 0) {
                            albums[albumIndex] = albums[albumIndex].run {
                                copy(
                                    songs = this.songs.toMutableList().apply { add(song) }
                                )
                            }
                        } else {
                            albums.add(
                                SongsSet(
                                    id = song.albumId,
                                    type = SongsSetType.Album,
                                    title = song.album,
                                    songs = listOf(song)
                                )
                            )
                        }
                    }
                    albums
                },
                artists = songs.let {
                    val artists = mutableListOf<SongsSet>()
                    it.forEach { song ->
                        val artistIndex = artists.indexOfFirst { it.id == song.artistId }
                        if (artistIndex >= 0) {
                            artists[artistIndex] = artists[artistIndex].run {
                                copy(
                                    songs = this.songs.toMutableList().apply { add(song) }
                                )
                            }
                        } else {
                            artists.add(
                                SongsSet(
                                    id = song.artistId,
                                    type = SongsSetType.Artist,
                                    title = song.artist,
                                    songs = listOf(song)
                                )
                            )
                        }
                    }
                    artists
                }
            )
        }
}