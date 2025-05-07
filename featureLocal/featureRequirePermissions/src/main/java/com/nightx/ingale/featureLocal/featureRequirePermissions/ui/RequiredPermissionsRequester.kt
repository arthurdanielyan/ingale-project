package com.nightx.ingale.featureLocal.featureRequirePermissions.ui

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.nightx.ingale.core.ui.flows.ObserveEffects
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsUiCallbacks
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsUiEffect
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsViewState
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui.components.AudioPermissionDescriptionProvider
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui.components.NotificationPermissionDescriptionProvider
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui.components.PermissionNotGrantedDialog
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui.components.StoragePermissionDescriptionProvider
import kotlinx.coroutines.flow.Flow

@Composable
fun RequiredPermissionsRequesterDialog(
    component: RequirePermissionsComponent,
) {
    val state by component.uiState.collectAsState()

    RequiredPermissionsRequesterDialogContent(
        state = state,
        effects = component.uiEffect,
        callbacks = component.uiCallbacks,
    )
}

@Composable
private fun RequiredPermissionsRequesterDialogContent(
    state: RequirePermissionsViewState,
    effects: Flow<RequirePermissionsUiEffect>,
    callbacks: RequirePermissionsUiCallbacks,
) {
    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach(callbacks::onPermissionResult)
        }
    )
    ObserveEffects(effects) { effect ->
        when (effect) {
            is RequirePermissionsUiEffect.RequestPermission -> {
                permissionsResultLauncher.launch(effect.permissions.toTypedArray())
            }
        }
    }

    val context = LocalContext.current
    if(state.activePermissionDialog != null) {
        PermissionNotGrantedDialog(
            isPermanentlyDeclined = context.getActivity()
                ?.shouldShowRequestPermissionRationale(state.activePermissionDialog)?.not() ?: true,
            descriptionProvider = when (state.activePermissionDialog) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> StoragePermissionDescriptionProvider()
                Manifest.permission.READ_MEDIA_AUDIO -> AudioPermissionDescriptionProvider()
                else -> NotificationPermissionDescriptionProvider()
            },
            onOkClick = {
                callbacks.onOkClick(state.activePermissionDialog)
            },
            onGoToAppSettings = {
                callbacks.onGoToSettingsClick(state.activePermissionDialog)
            }
        )
    }
}

@Composable
private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
