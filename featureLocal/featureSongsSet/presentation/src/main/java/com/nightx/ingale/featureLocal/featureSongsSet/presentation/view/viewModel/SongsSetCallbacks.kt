package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel

import androidx.compose.runtime.Immutable
import com.nightx.featureLocal.core.viewState.song.SongViewState

@Immutable
interface SongsSetCallbacks {

    fun onSongClick(song: SongViewState)
}