package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api

import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

internal interface CurrentPlaylistsBottomSheetComponent {

    val uiState: StateFlow<CurrentPlaylistsBottomSheetViewState>
    val uiCallbacks: CurrentPlaylistsBottomSheetUiCallbacks

    data class Params(
        val songId: Long
    )

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            params: Params,
            onCreateNewPlaylistClick: () -> Unit,
            onDismiss: () -> Unit,
        ): CurrentPlaylistsBottomSheetComponent
    }
}