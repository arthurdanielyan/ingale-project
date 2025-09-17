package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api

import com.nightx.ingale.core.decompose.AppComponentContext

internal interface SongOperationsBottomSheetComponent {

    val uiCallbacks: SongOperationsUiCallbacks
    val onDismiss: () -> Unit

    data class Params(
        val songId: Long
    )

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            params: Params,
            onSongOperationClickCallback: (SongOperation, Long) -> Unit,
            onDismiss: () -> Unit,
        ): SongOperationsBottomSheetComponent
    }
}
