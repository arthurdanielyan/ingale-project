package com.example.ingale.feature_local.required_permissions_requester_dialog.presentation

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
import com.example.ingale.core.di.ingaleDialogViewModels
import com.example.ingale.core.presentation.flows.ObserveEffects
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.AudioPermissionDescriptionProvider
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.NotificationPermissionDescriptionProvider
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.PermissionNotGrantedDialog
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components.StoragePermissionDescriptionProvider
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract.Effect
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract.Event
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RequiredPermissionsRequesterDialog(
    savedStateHandle: SavedStateHandle
) {
    val vm = ingaleDialogViewModels<RequiredPermissionsRequesterViewModel>(
        savedStateHandle = savedStateHandle
    )
    RequiredPermissionsRequesterDialogContent(
        state = vm.state.collectAsState().value,
        effects = vm.effect,
        sendEvent = vm::sendEvent
    )
}

@Composable
private fun RequiredPermissionsRequesterDialogContent(
    state: RequiredPermissionsRequesterContract.State,
    effects: Flow<Effect>,
    sendEvent: (RequiredPermissionsRequesterContract.Event) -> Unit,
) {
    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach { (permission, isGranted) ->
                sendEvent(Event.PermissionResult(permission, isGranted))
            }
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
    state.requiredPermissionDialogs.forEach { permission ->
        PermissionNotGrantedDialog(
            isPermanentlyDeclined = context.getActivity()
                ?.shouldShowRequestPermissionRationale(permission)?.not() ?: true,
            descriptionProvider = when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> StoragePermissionDescriptionProvider()
                Manifest.permission.READ_MEDIA_AUDIO -> AudioPermissionDescriptionProvider()
                else -> NotificationPermissionDescriptionProvider()
            },
            onOkClick = {
                sendEvent(Event.DialogEvent.OkClick(permission))
            },
            onGoToAppSettings = {
                sendEvent(Event.DialogEvent.GoToSettingsClick)
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
