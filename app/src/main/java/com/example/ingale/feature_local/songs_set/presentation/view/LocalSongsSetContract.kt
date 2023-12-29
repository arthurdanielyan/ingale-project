package com.example.ingale.feature_local.songs_set.presentation.view

import com.example.ingale.core.domain.model.Song
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.mvi.UiEffect
import com.example.ingale.mvi.UiEvent
import com.example.ingale.mvi.UiState

interface LocalSongsSetContract {

    sealed interface Event : UiEvent {
        class PlaySong(val song: Song) : Event
    }

    sealed interface Effect : UiEffect

    data class State(
        val songsSetInfo: SongsSet,
    ) : UiState
}