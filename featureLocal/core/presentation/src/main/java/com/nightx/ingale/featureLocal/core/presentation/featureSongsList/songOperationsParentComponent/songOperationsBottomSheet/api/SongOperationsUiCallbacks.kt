package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api

import androidx.compose.runtime.Immutable

@Immutable
internal interface SongOperationsUiCallbacks {

    fun onSongOperationClick(operation: SongOperation)
}