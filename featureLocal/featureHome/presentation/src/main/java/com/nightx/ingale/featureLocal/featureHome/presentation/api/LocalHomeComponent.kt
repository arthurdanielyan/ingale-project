package com.nightx.ingale.featureLocal.featureHome.presentation.api

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalHomeComponent {

    val uiState: StateFlow<LocalMainScreenViewState>
    val uiEffect: Flow<LocalMainUiEffect>
    val uiCallbacks: LocalMainCallbacks
    val songsListComponent: SongsListComponent
    val requirePermissionsComponent: RequirePermissionsComponent

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext
        ): LocalHomeComponent
    }
}