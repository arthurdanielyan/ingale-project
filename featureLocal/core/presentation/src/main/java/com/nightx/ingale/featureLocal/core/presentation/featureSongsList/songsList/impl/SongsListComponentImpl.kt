package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.impl

import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildContext
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.core.viewState.toComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.SongViewStateToDomainMapper
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListCallbacks
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListViewState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

internal class SongsListComponentImpl(
    appComponentContext: AppComponentContext,
    songs: StateFlow<List<SongViewState>>,
    songOperationsParentComponentFactory: SongOperationsParentComponent.Factory,
    songViewStateToDomainMapper: SongViewStateToDomainMapper,
    private val playbackUserActions: PlaybackUserActions,
) : SongsListComponent,
    SongsListCallbacks,
    AppComponentContext by appComponentContext {

    private val songs = songs.mapLatest {
        songViewStateToDomainMapper.mapList(it)
    }.stateIn(componentScope, SharingStarted.Eagerly, emptyList())

    override val uiState = songs.mapLatest {
        SongsListViewState(
            songs = it.toComposeList()
        )
    }.stateInWhileSubscribed(componentScope, SongsListViewState())

    override val uiCallbacks = this

    override val songOperationsParentComponent = songOperationsParentComponentFactory(
        appComponentContext = appChildContext("songOperationsParentComponent"),
    )

    override fun onSongClick(song: SongViewState) {
        playbackUserActions.submitNewPlaylistAndPlay(
            songs.value,
            uiState.value.songs.indexOf(song)
        )
    }

    override fun onSongOperationsClick(song: SongViewState) {
        songOperationsParentComponent.openSongOperationsBottomSheet(
            songId = song.id,
        )
    }
}
