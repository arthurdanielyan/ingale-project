package com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel

import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.core.audio_player.AudioPlayer
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.presentation.navigation.coreNavigation.getArgument
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination
import com.nightx.ingale.feature_local.songs_set.presentation.view.mappers.SongsSetNavArgMapper
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.LocalSongsSetContract.Effect
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.LocalSongsSetContract.State
import com.nightx.ingale.mvi.BaseViewModel
import com.nightx.ingale.mvi.wrappers.emptyStableList

class SongsSetViewModel(
    savedStateHandle: SavedStateHandle,
    songsSetNavArgMapper: SongsSetNavArgMapper
) : BaseViewModel<State, Effect>(), SongsSetCallbacks {

    init {
        savedStateHandle.getArgument<SongsSetScreenDestination.SongsSet>()?.let {
            updateState {
                copy(
                    songsSetInfo = songsSetNavArgMapper(it)
                )
            }
        }
    }

    override fun defineInitialState(): State =
        State(
            songsSetInfo = SongsSet(
                id = -1,
                title = "Loading...",
                songs = emptyStableList()
            )
        )

    override fun onSongClick(song: Song) {
        AudioPlayer.play(
            currentState.songsSetInfo.songs,
            currentState.songsSetInfo.songs.indexOf(song)
        )
    }
}