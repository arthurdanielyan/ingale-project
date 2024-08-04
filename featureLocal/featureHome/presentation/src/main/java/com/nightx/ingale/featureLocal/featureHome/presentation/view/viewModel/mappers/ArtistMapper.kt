package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.mapper.SongToViewStateMapper
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureHome.domain.model.Artist

internal class ArtistMapper(
    private val songToViewStateMapper: SongToViewStateMapper
) : Mapper<Artist, SongsSetViewState> {

    override fun map(from: Artist) = SongsSetViewState(
        id = from.artistId,
        title = from.artistName,
        songs = songToViewStateMapper.mapList(from.songs).toStableList()
    )
}
