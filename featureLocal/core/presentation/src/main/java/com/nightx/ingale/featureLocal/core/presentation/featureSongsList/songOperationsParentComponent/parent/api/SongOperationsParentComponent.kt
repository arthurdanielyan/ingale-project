package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.nightx.ingale.core.decompose.AppComponentContext

interface SongOperationsParentComponent {

    val childSlot: Value<ChildSlot<SongOperationsChildConfig, Any>>

    fun openSongOperationsBottomSheet(songId: Long)

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
        ): SongOperationsParentComponent
    }
}