package com.nightx.ingale.featureLocal.featureRequirePermissions.api

import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RequirePermissionsComponent {

    val uiState: StateFlow<RequirePermissionsViewState>
    val uiCallbacks: RequirePermissionsUiCallbacks
    val uiEffect: Flow<RequirePermissionsUiEffect>

    data class Params(
        val permissions: List<String>
    )

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            params: Params,
            onAudioPermissionResult: (Boolean) -> Unit,
        ): RequirePermissionsComponent
    }
}