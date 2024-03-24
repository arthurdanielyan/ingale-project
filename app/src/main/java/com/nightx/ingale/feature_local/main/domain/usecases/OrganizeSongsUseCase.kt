package com.nightx.ingale.feature_local.main.domain.usecases

import com.nightx.ingale.core.domain.CoroutineDispatchers
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.main.domain.model.Album
import com.nightx.ingale.feature_local.main.domain.model.Artist
import com.nightx.ingale.feature_local.main.domain.model.SongsSeparation
import com.nightx.ingale.mvi.wrappers.stableListOf
import com.nightx.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.withContext

class OrganizeSongsUseCase(
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(songs: List<Song>): SongsSeparation =
        withContext(dispatchers.default) {
            SongsSeparation(
                songs = songs,
                albums = songs.let {
                    val albums = mutableListOf<Album>()
                    it.forEach { song ->
                        val albumIndex = albums.indexOfFirst { it.albumId == song.albumId }
                        if (albumIndex >= 0) {
                            albums[albumIndex] = albums[albumIndex].run {
                                copy(
                                    songs = this.songs.toMutableList().apply { add(song) }
                                        .toStableList()
                                )
                            }
                        } else {
                            albums.add(
                                Album(
                                    albumId = song.albumId,
                                    albumName = song.album,
                                    songs = stableListOf(song)
                                )
                            )
                        }
                    }
                    albums
                },
                artists = songs.let {
                    val artists = mutableListOf<Artist>()
                    it.forEach { song ->
                        val artistIndex = artists.indexOfFirst { it.artistId == song.artistId }
                        if (artistIndex >= 0) {
                            artists[artistIndex] = artists[artistIndex].run {
                                copy(
                                    songs = this.songs.toMutableList().apply { add(song) }
                                        .toStableList()
                                )
                            }
                        } else {
                            artists.add(
                                Artist(
                                    artistId = song.artistId,
                                    artistName = song.artist,
                                    songs = stableListOf(song)
                                )
                            )
                        }
                    }
                    artists
                }
            )
        }
}