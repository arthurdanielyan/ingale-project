package com.nightx.ingale.core.data.model.mapper

import com.nightx.ingale.core.data.model.SongRealm
import com.nightx.ingale.core.domain.Mapper
import com.nightx.ingale.core.domain.model.Song

class SongRealmMapper : Mapper<SongRealm, Song> {

    override operator fun invoke(from: SongRealm): Song =
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