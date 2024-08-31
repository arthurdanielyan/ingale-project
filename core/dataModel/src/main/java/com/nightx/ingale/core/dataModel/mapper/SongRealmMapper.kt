package com.nightx.ingale.core.dataModel.mapper

import com.nightx.ingale.core.dataModel.SongRealm
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.Mapper

class SongRealmMapper : Mapper<SongRealm, Song> {

    override fun map(from: SongRealm): Song =
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