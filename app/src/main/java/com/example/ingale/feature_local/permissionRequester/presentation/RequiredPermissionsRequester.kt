package com.example.ingale.feature_local.permissionRequester.presentation

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
import com.example.ingale.core.di.ingaleViewModels
import com.example.ingale.core.presentation.ObserveEffects
import com.example.ingale.feature_local.permissionRequester.presentation.ui_components.AudioPermissionDescriptionProvider
import com.example.ingale.feature_local.permissionRequester.presentation.ui_components.NotificationPermissionDescriptionProvider
import com.example.ingale.feature_local.permissionRequester.presentation.ui_components.PermissionNotGrantedDialog
import com.example.ingale.feature_local.permissionRequester.presentation.ui_components.StoragePermissionDescriptionProvider
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterContract.Effect
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterContract.Event
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterViewModel

@Composable
fun RequiredPermissionsRequester() {
    val viewModel = ingaleViewModels<PermissionRequesterViewModel>()

    val state by viewModel.state.collectAsState()

    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach { (permission, isGranted) ->
                viewModel.sendEvent(Event.PermissionResult(permission, isGranted))
            }
        }
    )
    ObserveEffects(viewModel.effect) { effect ->
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
                viewModel.sendEvent(Event.DialogEvent.OkClick(permission))
            },
            onGoToAppSettings = {
                viewModel.sendEvent(Event.DialogEvent.GoToSettingsClick)
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
