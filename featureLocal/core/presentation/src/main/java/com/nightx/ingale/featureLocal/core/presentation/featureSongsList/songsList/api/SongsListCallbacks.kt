package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState

@Immutable
interface SongsListCallbacks {

    fun onSongClick(song: SongViewState)
    fun onSongOperationsClick(song: SongViewState)
}
