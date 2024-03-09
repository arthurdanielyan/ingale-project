package com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.core.presentation.navigation.destination.getOnResultCallback
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogNavigator
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract.Effect
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterContract.State
import com.nightx.ingale.mvi.BaseViewModel
import com.nightx.ingale.mvi.wrappers.emptyStableList
import com.nightx.ingale.mvi.wrappers.minus
import com.nightx.ingale.mvi.wrappers.plus
import com.nightx.ingale.mvi.wrappers.stableListOf
import com.nightx.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class RequiredPermissionsRequesterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dialogNavigator: DialogNavigator,
    private val applicationContext: Context,
) : BaseViewModel<State, Effect>(), RequiredPermissionsCallbacks {

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

    override fun defineInitialState(): State = State()

    private val requiredPermissionDialogs = MutableStateFlow(emptyStableList<String>())
    override val state: StateFlow<State> =
        combine(requiredPermissionDialogs) { requiredPermissionDialogs ->
            State(
                requiredPermissionDialogs = requiredPermissionDialogs[0]
            )
        }.viewModelState()

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
            sendEffect {
                Effect.RequestPermission(permissionsToRequest.toStableList())
            }
        } else {
            dialogNavigator.dismiss()
        }
        if(isAudioPermissionGranted) {
            savedStateHandle.getOnResultCallback<Boolean>()?.invoke(true)
        }
    }

    override fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && requiredPermissionDialogs.value.firstOrNull { it == permission } == null) {
            requiredPermissionDialogs.update { it + permission }
        } else if (isGranted) {
            requiredPermissionDialogs.update { it - permission }
        }
        if (permission == Manifest.permission.READ_MEDIA_AUDIO || permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
            savedStateHandle.getOnResultCallback<Boolean>()?.invoke(isGranted)
        }
    }

    override fun onOkClick(permission: String) {
        // This forces the dialog to be updated with a different button
        // as the same element will be inserted again
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
        if(requiredPermissionDialogs.value.isEmpty()) {
            dialogNavigator.dismiss()
        }
    }

    private val isAudioPermissionGranted: Boolean
        get() {
            val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            return ContextCompat.checkSelfPermission(
                applicationContext,
                audioPermission
            ) == PackageManager.PERMISSION_GRANTED
        }
}