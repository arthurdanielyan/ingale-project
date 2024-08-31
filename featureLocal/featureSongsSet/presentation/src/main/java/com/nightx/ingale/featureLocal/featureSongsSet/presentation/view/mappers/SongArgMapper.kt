package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers

import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongArg

class SongArgMapper : Mapper<SongArg, Song> {

    override fun map(from: SongArg) = Song(
        id = from.id,
        title = from.title,
        album = from.album,
        duration = from.duration,
        artist = from.artist,
        genre = from.genre,
        path = from.path,
        picturePath = from.picturePath,
        artistId = from.artistId,
        albumId = from.albumId,
        lastModified = from.lastModified,
    )
}