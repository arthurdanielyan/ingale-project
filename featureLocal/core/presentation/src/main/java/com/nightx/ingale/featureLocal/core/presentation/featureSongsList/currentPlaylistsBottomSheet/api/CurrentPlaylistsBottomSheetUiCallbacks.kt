package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.PlaylistViewState

@Immutable
interface CurrentPlaylistsBottomSheetUiCallbacks {

    fun onSaveToPlaylist(playlist: PlaylistViewState)

    fun onCreateNewPlaylistClick()

    fun onDismiss()
}