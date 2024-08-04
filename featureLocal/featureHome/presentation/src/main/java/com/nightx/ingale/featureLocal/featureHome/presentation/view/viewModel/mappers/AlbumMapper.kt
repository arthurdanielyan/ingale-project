package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.mapper.SongToViewStateMapper
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureHome.domain.model.Album

internal class AlbumMapper(
    private val songToViewStateMapper: SongToViewStateMapper
) : Mapper<Album, SongsSetViewState> {

    override fun map(from: Album) = SongsSetViewState(
        id = from.albumId,
        title = from.albumName,
        songs = songToViewStateMapper.mapList(from.songs).toStableList()
    )
}
