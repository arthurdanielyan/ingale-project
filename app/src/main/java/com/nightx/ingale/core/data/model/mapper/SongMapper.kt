package com.nightx.ingale.core.data.model.mapper

import com.nightx.ingale.core.data.Mapper
import com.nightx.ingale.core.data.model.SongRO
import com.nightx.ingale.core.domain.model.Song

class SongMapper : Mapper<Song, SongRO> {

    override fun mapToData(from: Song): SongRO =
        SongRO().apply {
            id = from.id
            title = from.title
            album = from.album
            duration = from.duration
            artist = from.artist
            genre = from.genre
            path = from.path
            artistId = from.artistId
            albumId = from.albumId
            lastModified = from.lastModified
        }

    override fun mapToDomain(from: SongRO): Song =
        Song(
            id = from.id,
            title = from.title,
            album = from.album,
            duration = from.duration,
            artist = from.artist,
            genre = from.genre,
            path = from.path,
            picture = null,
            artistId = from.artistId,
            albumId = from.albumId,
            lastModified = from.lastModified,
        )

}