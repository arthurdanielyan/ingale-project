package com.nightx.ingale.featureLocal.core.presentation.common.viewState

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.viewState.ComposeList
import com.nightx.ingale.core.viewState.emptyComposeList

@Immutable
data class SongsSetViewState(
    val id: Long = -1,
    val title: String = "Loading...",
    val songs: ComposeList<SongViewState> = emptyComposeList(),
    val iconPath: String = "",
)