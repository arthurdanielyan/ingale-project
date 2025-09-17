package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.impl

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperation
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsUiCallbacks

internal class SongOperationsBottomSheetComponentImpl(
    appComponentContext: AppComponentContext,
    private val params: SongOperationsBottomSheetComponent.Params,
    private val onSongOperationClickCallback: (SongOperation, Long) -> Unit,
    override val onDismiss: () -> Unit
) : AppComponentContext by appComponentContext,
    SongOperationsBottomSheetComponent, SongOperationsUiCallbacks {

    override val uiCallbacks = this

    override fun onSongOperationClick(operation: SongOperation) {
        onSongOperationClickCallback(operation, params.songId)
    }
}