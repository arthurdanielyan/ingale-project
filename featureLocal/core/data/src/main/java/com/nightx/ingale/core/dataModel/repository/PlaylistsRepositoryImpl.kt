package com.nightx.ingale.core.dataModel.repository

import com.nightx.ingale.core.dataModel.mapper.PlaylistWithSongsMapper
import com.nightx.ingale.core.dataModel.room.playlists.PlaylistsDao
import com.nightx.ingale.core.dataModel.room.playlists.entity.PlaylistEntity
import com.nightx.ingale.core.dataModel.room.playlists.entity.relations.PlaylistSongCrossRef
import com.nightx.ingale.core.domain.playlists.exceptions.PlaylistAlreadyExistsException
import com.nightx.ingale.core.domain.playlists.model.Playlist
import com.nightx.ingale.core.domain.playlists.repository.PlaylistsRepository
import com.nightx.ingale.core.utils.mapList

class PlaylistsRepositoryImpl(
    private val playlistsDao: PlaylistsDao,
    private val playlistWithSongsMapper: PlaylistWithSongsMapper,
) : PlaylistsRepository {

    override suspend fun getPlaylists(): List<Playlist> {
        return playlistWithSongsMapper.mapList(
            playlistsDao.getPlaylists()
        )
    }

    override suspend fun createNewPlaylist(name: String, initialSongId: Long?): Result<Unit> {
        if (
            playlistsDao.createPlaylist(
                playlist = PlaylistEntity(name = name)
            ) < 0
        ) {
            return Result
                .failure(PlaylistAlreadyExistsException(playlistName = name))
        }

        initialSongId?.let {
            playlistsDao.insertPlaylistSongCrossRef(
                crossRef = PlaylistSongCrossRef(
                    playlistName = name,
                    songId = it
                )
            )
        }
        return Result.success(Unit)
    }

    override suspend fun addSongToPlaylist(songId: Long, playlistName: String) {
        playlistsDao.insertPlaylistSongCrossRef(
            crossRef = PlaylistSongCrossRef(
                playlistName = playlistName,
                songId = songId
            )
        )
    }
}