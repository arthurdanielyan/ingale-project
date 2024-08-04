package com.nightx.ingale.featureLocal.featureRequirePermissions.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.presentation.viewModel.UiEffect
import com.nightx.ingale.core.presentation.viewModel.UiState
import com.nightx.ingale.core.viewState.StableList

interface RequiredPermissionsRequesterContract {

    sealed interface Effect : UiEffect {
        data class RequestPermission(val permissions: StableList<String>) : Effect
    }

    @Immutable
    data class State(
        val activePermissionDialog: String? = null
    ) : UiState
}