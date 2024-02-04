package com.night.ingale.feature_local.songs_set.presentation.view

import com.night.ingale.core.domain.model.Song
import com.night.ingale.feature_local.local_core.domain.model.SongsSet
import com.night.ingale.mvi.UiEffect
import com.night.ingale.mvi.UiEvent
import com.night.ingale.mvi.UiState

interface LocalSongsSetContract {

    sealed interface Event : UiEvent {
        class PlaySong(val song: Song) : Event
    }

    sealed interface Effect : UiEffect

    data class State(
        val songsSetInfo: SongsSet,
    ) : UiState
}