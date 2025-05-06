package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel

import androidx.compose.runtime.Immutable
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState

@Immutable
interface SongsSetCallbacks {
    fun onSongClick(song: SongViewState)
    fun onBackClick()
}