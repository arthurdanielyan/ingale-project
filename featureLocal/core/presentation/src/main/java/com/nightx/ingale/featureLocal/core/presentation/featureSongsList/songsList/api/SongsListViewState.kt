package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api

import com.nightx.ingale.core.viewState.ComposeList
import com.nightx.ingale.core.viewState.emptyComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState

data class SongsListViewState(
    val songs: ComposeList<SongViewState> = emptyComposeList(),
)
