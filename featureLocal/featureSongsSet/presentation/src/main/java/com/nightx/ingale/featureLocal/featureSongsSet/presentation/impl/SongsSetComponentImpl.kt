package com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl

import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domainModel.model.Song
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent.Params
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongArgMapper
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongsSetParamsMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class SongsSetComponentImpl(
    appComponentContext: AppComponentContext,
    params: Params,
    songsSetParamsMapper: SongsSetParamsMapper,
    songArgMapper: SongArgMapper,
    private val playbackUserActions: PlaybackUserActions,
) : SongsSetComponent,
    SongsSetUiCallbacks,
    AppComponentContext by appComponentContext {

    private val songs: List<Song> = songArgMapper.mapList(params.songs)

    override val uiState: StateFlow<SongsSetViewState> = MutableStateFlow(
        songsSetParamsMapper.map(params)
    )
    override val uiCallbacks = this

    override fun onSongClick(song: SongViewState) {
        playbackUserActions.submitNewPlaylistAndPlay(
            songs,
            uiState.value.songs.indexOf(song)
        )
    }

    override fun onBackClick() {
        appRouter.pop()
    }
}