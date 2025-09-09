package com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers

import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.toComposeList
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSet

internal class SongsSetViewStateMapper(
    private val songViewStateMapper: SongViewStateMapper,
) : Mapper<SongsSet, SongsSetViewState> {

    override fun map(from: SongsSet) = SongsSetViewState(
        id = from.id,
        title = from.title,
        songs = songViewStateMapper.mapList(from.songs).toComposeList()
    )
}
