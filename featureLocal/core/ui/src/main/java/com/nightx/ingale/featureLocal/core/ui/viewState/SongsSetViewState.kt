package com.nightx.ingale.featureLocal.core.ui.viewState

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.viewState.StableList
import com.nightx.ingale.core.viewState.emptyStableList

@Immutable
data class SongsSetViewState(
    val id: Long = -1,
    val title: String = "Loading...",
    val songs: StableList<SongViewState> = emptyStableList(),
    val iconPath: String = "",
)