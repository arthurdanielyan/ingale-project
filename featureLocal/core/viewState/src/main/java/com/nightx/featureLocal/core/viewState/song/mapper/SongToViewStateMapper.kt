package com.nightx.featureLocal.core.viewState.song.mapper

import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.Mapper

class SongToViewStateMapper : Mapper<Song, SongViewState> {

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
