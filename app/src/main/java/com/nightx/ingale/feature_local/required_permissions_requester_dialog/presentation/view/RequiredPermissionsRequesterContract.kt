package com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.mvi.UiEffect
import com.nightx.ingale.mvi.UiState
import com.nightx.ingale.mvi.wrappers.StableList

interface RequiredPermissionsRequesterContract {

    sealed interface Effect : UiEffect {
        data class RequestPermission(val permissions: StableList<String>) : Effect
    }

    @Immutable
    data class State(
        val activePermissionDialog: String? = null
    ) : UiState
}