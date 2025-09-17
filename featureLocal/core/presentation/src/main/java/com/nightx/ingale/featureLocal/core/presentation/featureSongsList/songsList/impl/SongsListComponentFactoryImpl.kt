package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.impl

import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.SongViewStateToDomainMapper
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import kotlinx.coroutines.flow.StateFlow

internal class SongsListComponentFactoryImpl(
    private val songOperationsParentComponentFactory: SongOperationsParentComponent.Factory,
    private val songViewStateToDomainMapper: SongViewStateToDomainMapper,
    private val playbackUserActions: PlaybackUserActions,
) : SongsListComponent.Factory {

    override operator fun invoke(
        appComponentContext: AppComponentContext,
        songs: StateFlow<List<SongViewState>>,
    ): SongsListComponent {
        return SongsListComponentImpl(
            appComponentContext = appComponentContext,
            songs = songs,
            songOperationsParentComponentFactory = songOperationsParentComponentFactory,
            songViewStateToDomainMapper = songViewStateToDomainMapper,
            playbackUserActions = playbackUserActions,
        )
    }
}
