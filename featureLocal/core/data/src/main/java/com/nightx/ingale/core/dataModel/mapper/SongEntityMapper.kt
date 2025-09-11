package com.nightx.ingale.core.dataModel.mapper

import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.utils.Mapper

class SongEntityMapper : Mapper<SongEntity, Song> {

    override fun map(from: SongEntity): Song =
        Song(
            id = from.id,
            title = from.title,
            album = from.album,
            duration = from.duration,
            artist = from.artist,
            genre = from.genre,
            path = from.path,
            picturePath = from.previewPath,
            artistId = from.artistId,
            albumId = from.albumId,
            lastModified = from.lastModified,
        )
}