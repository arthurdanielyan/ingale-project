package com.nightx.ingale.featureLocal.core.ui.viewState.mapper

import com.nightx.ingale.core.domainModel.model.Song
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState

class SongViewStateMapper : Mapper<Song, SongViewState> {

    override fun map(from: Song) = SongViewState(
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
        lastModifier = from.lastModified
    )
}
