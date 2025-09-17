package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.impl

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperation
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent

internal class SongOperationsBottomSheetComponentFactoryImpl :
    SongOperationsBottomSheetComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        params: SongOperationsBottomSheetComponent.Params,
        onSongOperationClickCallback: (SongOperation, Long) -> Unit,
        onDismiss: () -> Unit
    ): SongOperationsBottomSheetComponent {
        return SongOperationsBottomSheetComponentImpl(
            appComponentContext = appComponentContext,
            params = params,
            onSongOperationClickCallback = onSongOperationClickCallback,
            onDismiss = onDismiss,
        )
    }
}