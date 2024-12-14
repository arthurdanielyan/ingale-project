package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongArg
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination

internal class SongsSetToNavArgMapper :
    Mapper<SongsSetViewState, SongsSetScreenDestination.SongsSetArgs> {

    override fun map(from: SongsSetViewState): SongsSetScreenDestination.SongsSetArgs {
        return SongsSetScreenDestination.SongsSetArgs(
            id = from.id,
            title = from.title,
            songs = from.songs.map {
                SongArg(
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
                    lastModified = it.lastModifier
                )
            },
            iconPath = from.iconPath
        )
    }
}