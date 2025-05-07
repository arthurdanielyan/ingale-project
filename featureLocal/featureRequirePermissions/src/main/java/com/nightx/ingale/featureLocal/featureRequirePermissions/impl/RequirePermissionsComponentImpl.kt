package com.nightx.ingale.featureLocal.featureRequirePermissions.impl

import com.arkivanov.essenty.lifecycle.doOnStart
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.presentation.osExt.api.PermissionInspector
import com.nightx.ingale.core.presentation.osExt.api.SettingsLauncher
import com.nightx.ingale.core.presentation.viewModel.UiEffectSender
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.core.utils.yap
import com.nightx.ingale.core.viewState.composeListOf
import com.nightx.ingale.core.viewState.toComposeList
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsUiCallbacks
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsUiEffect
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class RequirePermissionsComponentImpl(
    appComponentContext: AppComponentContext,
    private val params: RequirePermissionsComponent.Params,
    private val onAudioPermissionResult: (Boolean) -> Unit,
    private val settingsLauncher: SettingsLauncher,
    private val permissionInspector: PermissionInspector
) : RequirePermissionsComponent,
    RequirePermissionsUiCallbacks,
    UiEffectSender<RequirePermissionsUiEffect>(),
    AppComponentContext by appComponentContext {

    private val requiredPermissionDialogs = MutableStateFlow(emptyList<String>())

    override val uiCallbacks = this

    override val uiState: StateFlow<RequirePermissionsViewState> =
        requiredPermissionDialogs.map { requiredPermissionDialogs ->
            RequirePermissionsViewState(
                activePermissionDialog = requiredPermissionDialogs.firstOrNull()
            )
        }.stateInWhileSubscribed(componentScope, RequirePermissionsViewState())

    private fun checkPermissions() {
        val permissionsToRequest = params.permissions.filter(
            permissionInspector::isPermissionDenied
        )
        if (permissionsToRequest.isNotEmpty()) {
            if (permissionsToRequest.contains(permissionInspector.audioPermissionKey)) {
                onAudioPermissionResult(false)
            }
            requiredPermissionDialogs.update {
                it.filter { permissionDialog ->
                    permissionsToRequest.contains(permissionDialog)
                }
            }
            sendEffect {
                RequirePermissionsUiEffect.RequestPermission(
                    permissionsToRequest.toComposeList()
                )
            }
        } else {
            requiredPermissionDialogs.update { emptyList() }
            onAudioPermissionResult(true)
        }
    }

    init {
        doOnStart {
            checkPermissions()
            yap("requirePermissionsComponent doOnStart")
        }
    }

    override fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && requiredPermissionDialogs.value.firstOrNull { it == permission } == null) {
            requiredPermissionDialogs.update { it + permission }
        } else if (isGranted) {
            requiredPermissionDialogs.update { it - permission }
        }
        if (permission == permissionInspector.audioPermissionKey) {
            onAudioPermissionResult(isGranted)
        }
    }

    override fun onOkClick(permission: String) {
        requiredPermissionDialogs.update { it - permission }
        sendEffect {
            RequirePermissionsUiEffect.RequestPermission(composeListOf(permission))
        }
    }

    override fun onGoToSettingsClick(permission: String) {
//        requiredPermissionDialogs.update { it - permission }
        settingsLauncher.goToAppSettings()
    }
}