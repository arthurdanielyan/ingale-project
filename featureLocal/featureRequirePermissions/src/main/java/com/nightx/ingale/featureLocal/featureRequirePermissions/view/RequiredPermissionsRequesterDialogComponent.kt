package com.nightx.ingale.featureLocal.featureRequirePermissions.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.nightx.ingale.core.presentation.dialogComponent.DialogComponent
import com.nightx.ingale.core.viewState.emptyStableList
import com.nightx.ingale.core.viewState.minus
import com.nightx.ingale.core.viewState.plus
import com.nightx.ingale.core.viewState.stableListOf
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterContract.Effect
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterContract.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RequiredPermissionsRequesterDialogComponent(
    private val applicationContext: Context,
) : DialogComponent<State, Effect, Boolean>(), RequiredPermissionsCallbacks {

    private val requiredPermissions = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_AUDIO)
    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val audioPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    override fun defineInitialState(): State = State()

    private val requiredPermissionDialogs = MutableStateFlow(emptyStableList<String>())
    override val state: StateFlow<State> =
        requiredPermissionDialogs.map { requiredPermissionDialogs ->
            State(
                activePermissionDialog = requiredPermissionDialogs.firstOrNull()
            )
        }.dialogState()

    init {
        checkPermissions()
    }

    private fun checkPermissions() {
        val permissionsToRequest = requiredPermissions.filter { requiredPermission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                requiredPermission
            ) == PackageManager.PERMISSION_DENIED
        }
        if (permissionsToRequest.isNotEmpty()) {
            if (permissionsToRequest.contains(audioPermission)) {
                sendVmCallback(false)
            }
            sendEffect {
                Effect.RequestPermission(permissionsToRequest.toStableList())
            }
        } else {
            dismiss()
        }
    }

    override fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && requiredPermissionDialogs.value.firstOrNull { it == permission } == null) {
            requiredPermissionDialogs.update { it + permission }
        } else if (isGranted) {
            requiredPermissionDialogs.update { it - permission }
        }
        if (permission == audioPermission) {
            sendVmCallback(isGranted)
        }
    }

    override fun onOkClick(permission: String) {
        requiredPermissionDialogs.update { it - permission }
        sendEffect {
            Effect.RequestPermission(stableListOf(permission))
        }
    }

    override fun onGoToSettingsClick(permission: String) {
        requiredPermissionDialogs.update { it - permission }
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationContext.packageName, null)
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(this)
        }
        if (requiredPermissionDialogs.value.isEmpty()) {
            dismiss()
        }
    }
}