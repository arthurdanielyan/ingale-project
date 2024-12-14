package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination

class SongsSetArgMapper : Mapper<SongsSetScreenDestination.SongsSetArgs, SongsSetViewState> {

    override fun map(from: SongsSetScreenDestination.SongsSetArgs) =
        SongsSetViewState(
            id = from.id,
            title = from.title,
            songs = from.songs.map {
                SongViewState(
                    id = it.id,
                    title = it.title,
                    album = it.album,
                    duration = it.duration,
                    artist = it.artist,
                    genre = it.genre,
                    path = it.path,
                    picturePath = it.picturePath,
                    artistId = it.artistId,
                    albumId = it.albumId,
                    lastModifier = it.lastModified
                )
            }.toStableList(),
            iconPath = from.iconPath
        )
}
