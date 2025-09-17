package com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl

import androidx.compose.runtime.Immutable
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState

@Immutable
interface SongsSetUiCallbacks {
    fun onSongClick(song: SongViewState)
    fun onBackClick()
}