package com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers

import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent

class SongsSetParamsMapper : Mapper<SongsSetComponent.Params, SongsSetViewState> {

    override fun map(from: SongsSetComponent.Params) =
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
