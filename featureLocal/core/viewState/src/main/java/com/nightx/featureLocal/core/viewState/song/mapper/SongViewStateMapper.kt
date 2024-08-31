package com.nightx.featureLocal.core.viewState.song.mapper

import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.domainModel.DomainConstants
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.resources.strings.StringProvider
import com.nightx.ingale.resources.strings.R.string as Strings

class SongViewStateMapper(
    private val stringProvider: StringProvider,
) : Mapper<Song, SongViewState> {

    override fun map(from: Song) = SongViewState(
        id = from.id,
        title = from.title,
        album = if (from.album == DomainConstants.UNKNOWN_SONG_DATA_ID) {
            stringProvider.string(Strings.unknown_album)
        } else {
            from.album
        },
        duration = from.duration,
        artist = if (from.artist == DomainConstants.UNKNOWN_SONG_DATA_ID) {
            stringProvider.string(Strings.unknown_artist)
        } else {
            from.artist
        },
        genre = from.genre,
        path = from.path,
        picturePath = from.picturePath,
        artistId = from.artistId,
        albumId = from.albumId,
        lastModifier = from.lastModified
    )
}
