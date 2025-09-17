package com.nightx.ingale.core.dataModel.mapper

import com.nightx.ingale.core.dataModel.room.playlists.entity.relations.PlaylistWithSongs
import com.nightx.ingale.core.domain.playlists.model.Playlist
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.utils.mapList

class PlaylistWithSongsMapper(
    private val songEntityMapper: SongEntityMapper,
) : Mapper<PlaylistWithSongs, Playlist> {

    override fun map(from: PlaylistWithSongs): Playlist {
        return Playlist(
            name = from.playlist.name,
            songs = songEntityMapper.mapList(from.songs)
        )
    }
}