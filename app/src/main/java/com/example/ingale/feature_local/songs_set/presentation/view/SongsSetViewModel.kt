package com.example.ingale.feature_local.songs_set.presentation.view

import androidx.lifecycle.SavedStateHandle
import com.example.ingale.core.audio_player.AudioPlayer
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.main_navigation.Screens
import com.example.ingale.mvi.BaseViewModel
import com.example.ingale.feature_local.songs_set.presentation.view.LocalSongsSetContract.Effect
import com.example.ingale.feature_local.songs_set.presentation.view.LocalSongsSetContract.Event
import com.example.ingale.feature_local.songs_set.presentation.view.LocalSongsSetContract.State
import com.example.ingale.mvi.wrappers.emptyStableList

class SongsSetViewModel(
    private val navigator: LocalSongsSetNavigator,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<State, Event, Effect>() {

    init {
        savedStateHandle.get<SongsSet>(Screens.Local.SongsSetScreen.ARG_SONGS_SET).let {
            updateState {
                copy(
                    songsSetInfo = it!!
                )
            }
        }
    }

    override fun defineInitialState(): State =
        State(
            songsSetInfo = SongsSet(
                id = -1,
                title = "Loading...",
                songs = emptyStableList(),
                icon = null
            )
        )


    override fun handleEvent(event: Event) {
        when(event) {
            is Event.PlaySong -> {
                AudioPlayer.play(
                    currentState.songsSetInfo.songs,
                    currentState.songsSetInfo.songs.indexOf(event.song)
                )
            }
        }
    }
}