package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import kotlinx.coroutines.flow.StateFlow

interface SongsListComponent : AppComponentContext {

    val uiState: StateFlow<SongsListViewState>
    val uiCallbacks: SongsListCallbacks
    val songOperationsParentComponent: SongOperationsParentComponent

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            songs: StateFlow<List<SongViewState>>,
        ): SongsListComponent
    }
}
