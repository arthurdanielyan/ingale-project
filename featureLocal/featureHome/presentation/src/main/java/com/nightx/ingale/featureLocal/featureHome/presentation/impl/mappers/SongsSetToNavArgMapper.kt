package com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers

import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.navigation.api.LocalScreenConfig
import com.nightx.ingale.featureLocal.navigation.api.LocalScreenConfig.SongsSet.SongArg

internal class SongsSetToNavArgMapper :
    Mapper<SongsSetViewState, LocalScreenConfig.SongsSet> {

    override fun map(from: SongsSetViewState): LocalScreenConfig.SongsSet {
        return LocalScreenConfig.SongsSet(
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