package com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view

import androidx.compose.runtime.Immutable
import com.example.ingale.mvi.UiEffect
import com.example.ingale.mvi.UiEvent
import com.example.ingale.mvi.UiState
import com.example.ingale.mvi.wrappers.StableList
import com.example.ingale.mvi.wrappers.emptyStableList

interface RequiredPermissionsRequesterContract {

    sealed interface Event : UiEvent {
        data class PermissionResult(
            val permission: String,
            val isGranted: Boolean
        ): Event

        sealed interface DialogEvent : Event {
            data class OkClick(val permission: String): DialogEvent
            data object GoToSettingsClick: DialogEvent
        }
    }

    sealed interface Effect : UiEffect {
        data class RequestPermission(val permissions: StableList<String>) : Effect
    }

    @Immutable
    data class State(
        val requiredPermissionDialogs: StableList<String> = emptyStableList()
    ) : UiState
}