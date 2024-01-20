package com.example.ingale.feature_local.permissionRequester.presentation.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterContract.Effect
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterContract.Event
import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterContract.State
import com.example.ingale.mvi.BaseViewModel
import com.example.ingale.mvi.wrappers.emptyStableList
import com.example.ingale.mvi.wrappers.plus
import com.example.ingale.mvi.wrappers.minus
import com.example.ingale.mvi.wrappers.stableListOf
import com.example.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class PermissionRequesterViewModel(
    private val applicationContext: Context,
) : BaseViewModel<State, Event, Effect>() {

    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_AUDIO)
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
        val permissionsToRequest = requiredPermissions.filter { requiredPermission ->
            ContextCompat.checkSelfPermission(applicationContext, requiredPermission) == PackageManager.PERMISSION_DENIED
        }
        sendEffect {
            Effect.RequestPermission(permissionsToRequest.toStableList())
        }
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.DialogEvent.GoToSettingsClick -> {
                onGoToSettingsClick()
            }

            is Event.DialogEvent.OkClick -> onOkClick(event.permission)

            is Event.PermissionResult -> {
                onPermissionResult(event.permission, event.isGranted)
            }
        }
    }

    private fun onOkClick(permission: String) {
        // This forces the dialog to be updated with a different button
        // as the same element will be inserted again
        requiredPermissionDialogs.value = requiredPermissionDialogs.value - permission
        sendEffect {
            Effect.RequestPermission(stableListOf(permission))
        }
    }

    private fun onGoToSettingsClick() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationContext.packageName, null)
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(this)
        }
    }

    private fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && requiredPermissionDialogs.value.firstOrNull { it == permission } == null) {
            requiredPermissionDialogs.value = requiredPermissionDialogs.value + permission
        } else if(isGranted) {
            requiredPermissionDialogs.value = requiredPermissionDialogs.value - permission
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("myLogs", "ViewModelCleared")
    }
}