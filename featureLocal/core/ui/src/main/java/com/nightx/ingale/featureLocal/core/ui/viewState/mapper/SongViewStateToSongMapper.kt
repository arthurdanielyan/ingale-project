package com.nightx.ingale.featureLocal.core.ui.viewState.mapper

import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState

class SongViewStateToSongMapper : Mapper<SongViewState, Song> {

    override fun map(from: SongViewState) = Song(
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
        lastModified = from.lastModifier
    )
}