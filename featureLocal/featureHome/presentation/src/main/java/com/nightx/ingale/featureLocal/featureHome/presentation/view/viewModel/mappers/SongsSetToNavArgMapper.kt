package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.feature_local.local_navigation.destinations.SongArg
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination

internal class SongsSetToNavArgMapper : Mapper<SongsSetViewState, SongsSetScreenDestination.SongsSet> {

    override fun map(from: SongsSetViewState): SongsSetScreenDestination.SongsSet {
        return SongsSetScreenDestination.SongsSet(
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