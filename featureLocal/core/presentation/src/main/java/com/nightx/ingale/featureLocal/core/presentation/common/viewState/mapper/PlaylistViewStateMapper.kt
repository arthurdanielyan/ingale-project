package com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper

import com.nightx.ingale.core.domain.playlists.model.Playlist
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.PlaylistViewState

class PlaylistViewStateMapper : Mapper<Playlist, PlaylistViewState> {

    override fun map(from: Playlist): PlaylistViewState {
        return PlaylistViewState(
            name = from.name,
        )
    }
}