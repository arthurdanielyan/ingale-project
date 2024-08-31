package com.nightx.ingale.featureLocal.featureRequirePermissions

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
import com.nightx.ingale.core.presentation.dialogComponent.DialogComponentHolder
import com.nightx.ingale.core.presentation.dialogComponent.DialogComponentView
import com.nightx.ingale.core.presentation.flows.ObserveEffects
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui_components.AudioPermissionDescriptionProvider
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui_components.NotificationPermissionDescriptionProvider
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui_components.PermissionNotGrantedDialog
import com.nightx.ingale.featureLocal.featureRequirePermissions.ui_components.StoragePermissionDescriptionProvider
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsCallbacks
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterContract
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterContract.Effect
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterDialogComponent
import kotlinx.coroutines.flow.Flow

@Composable
fun RequiredPermissionsRequesterDialog(
    componentHolder: DialogComponentHolder<RequiredPermissionsRequesterDialogComponent>,
) {
    DialogComponentView(
        componentHolder = componentHolder,
    ) { component ->
        val state by component.state.collectAsState()
        RequiredPermissionsRequesterDialogContent(
            state = state,
            effects = component.effect,
            callbacks = component
        )
    }
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
