package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.viewState.ComposeList
import com.nightx.ingale.core.viewState.emptyComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.PlaylistViewState

@Immutable
data class CurrentPlaylistsBottomSheetViewState(
    val playlists: ComposeList<PlaylistViewState> = emptyComposeList(),
)