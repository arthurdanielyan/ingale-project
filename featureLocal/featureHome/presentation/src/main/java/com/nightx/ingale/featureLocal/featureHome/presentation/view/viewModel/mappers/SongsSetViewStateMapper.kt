package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateMapper
import com.nightx.ingale.core.domainModel.DomainConstants
import com.nightx.ingale.core.utils.Mapper
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSet
import com.nightx.ingale.featureLocal.featureHome.domain.model.SongsSetType
import com.nightx.ingale.resources.strings.StringProvider
import com.nightx.ingale.resources.strings.R.string as Strings

internal class SongsSetViewStateMapper(
    private val songViewStateMapper: SongViewStateMapper,
    private val stringProvider: StringProvider,
) : Mapper<SongsSet, SongsSetViewState> {

    override fun map(from: SongsSet) = SongsSetViewState(
        id = from.id,
        title = if (from.title == DomainConstants.UNKNOWN_SONG_DATA_ID) {
            when (from.type) {
                SongsSetType.Album -> stringProvider.string(Strings.unknown_album)
                SongsSetType.Artist -> stringProvider.string(Strings.unknown_artist)
                SongsSetType.Playlist -> stringProvider.string(Strings.unknown)
            }
        } else {
            from.title
        },
        songs = songViewStateMapper.mapList(from.songs).toStableList()
    )
}
