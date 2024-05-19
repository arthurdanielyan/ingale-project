package com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.core.presentation.di.ingaleViewModels
import com.nightx.ingale.core.presentation.flows.ObserveEffects
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.AudioPermissionDescriptionProvider
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.NotificationPermissionDescriptionProvider
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.PermissionNotGrantedDialog
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.StoragePermissionDescriptionProvider
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsCallbacks
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract.Effect
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RequiredPermissionsRequesterDialog(
    savedStateHandle: SavedStateHandle,
) {
    val vm = ingaleViewModels<RequiredPermissionsRequesterViewModel>(
        savedStateHandle = savedStateHandle
    )

    RequiredPermissionsRequesterDialogContent(
        state = vm.state.collectAsState().value,
        effects = vm.effect,
        callbacks = vm
    )
}

@Composable
private fun RequiredPermissionsRequesterDialogContent(
    state: RequiredPermissionsRequesterContract.State,
    effects: Flow<Effect>,
    callbacks: RequiredPermissionsCallbacks,
) {
    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach(callbacks::onPermissionResult)
        }
    )
    ObserveEffects(effects) { effect ->
        when (effect) {
            is Effect.RequestPermission -> {
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
