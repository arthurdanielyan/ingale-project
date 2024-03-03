package com.nightx.ingale.feature_local.songs_set.presentation.view

import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.mvi.UiEffect
import com.nightx.ingale.mvi.UiEvent
import com.nightx.ingale.mvi.UiState

interface LocalSongsSetContract {

    sealed interface Event : UiEvent {
        class PlaySong(val song: Song) : Event
    }

    sealed interface Effect : UiEffect

    data class State(
        val songsSetInfo: SongsSet,
    ) : UiState
}