package com.example.ingale.feature_local.main.presentation.components

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ingale.core.presentation.ObserveEffects
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.Event.PermissionResult
import com.example.ingale.mvi.wrappers.StableList
import kotlinx.coroutines.flow.Flow

@Composable
fun PermissionRequester(
    requiredPermissions: Array<String>,
    visiblePermissionDialogQueue: StableList<String>,
    onDismiss: () -> Unit,
    onPermissionResult: (PermissionResult) -> Unit,
    requestPermissionsEffect: Flow<LocalMainContract.Effect.RequestPermissions>
) {
    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach {
                onPermissionResult(PermissionResult(it.key, it.value))
            }
        }
    )
    ObserveEffects(requestPermissionsEffect) {
        permissionsResultLauncher.launch(
            requiredPermissions
        )
    }

    val context = LocalContext.current
    visiblePermissionDialogQueue
        .reversed()
        .forEach {
            PermissionNotGrantedDialog(
                isPermanentlyDeclined = context.getActivity()
                    ?.shouldShowRequestPermissionRationale(it)?.not() ?: true,
                descriptionProvider =
                when (it) {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    -> AudioPermissionDescriptionProvider()

                    else -> NotificationPermissionDescriptionProvider()
                },
                onDismiss = onDismiss,
                onOkClick = {
                    onDismiss()
                    permissionsResultLauncher.launch(
                        arrayOf(it)
                    )
                },
                onGoToAppSettings = {
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    ).also(context::startActivity)
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
