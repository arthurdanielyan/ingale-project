package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel

import androidx.lifecycle.SavedStateHandle
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.navigation.getArgument
import com.nightx.ingale.core.presentation.viewModel.BaseViewModel
import com.nightx.ingale.core.presentation.viewModel.UiEffect
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers.SongArgMapper
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers.SongsSetArgMapper
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination

internal class SongsSetViewModel(
    savedStateHandle: SavedStateHandle,
    songsSetArgMapper: SongsSetArgMapper,
    songArgMapper: SongArgMapper,
    private val playerUiActions: PlayerUiActions,
) : BaseViewModel<SongsSetViewState, UiEffect>(), SongsSetCallbacks {

    private val songs: List<Song> =
        savedStateHandle.getArgument<SongsSetScreenDestination.SongsSet>()?.let {
            updateState {
                songsSetArgMapper(it)
            }
            songArgMapper.mapList(it.songs)
        } ?: emptyList()

    override fun defineInitialState() = SongsSetViewState.Empty

    override fun onSongClick(song: SongViewState) {
        playerUiActions.submitNewListAndPlay(
            songs,
            currentState.songs.indexOf(song)
        )
    }
}