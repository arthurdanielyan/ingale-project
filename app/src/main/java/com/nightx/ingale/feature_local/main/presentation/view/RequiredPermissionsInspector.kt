package com.nightx.ingale.feature_local.main.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogDestination.RequiredPermissionRequesterDestination
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogNavigator

class RequiredPermissionsInspector(
    private val applicationContext: Context,
    private val dialogNavigator: DialogNavigator,
) {

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

    private var wasReloadCommandSent = false

    fun start(shouldReloadSongs: (Boolean) -> Unit) {
        checkPermissions(shouldReloadSongs)
    }

    private fun checkPermissions(shouldReloadSongs: (Boolean) -> Unit) {
        val permissionsToRequest = requiredPermissions.filter { requiredPermission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                requiredPermission
            ) == PackageManager.PERMISSION_DENIED
        }
        if (permissionsToRequest.isNotEmpty()) {
            dialogNavigator.activate<RequiredPermissionRequesterDestination.ResultData>(
                destination = RequiredPermissionRequesterDestination,
                onResult = {
                    shouldReloadSongs(it.audioPermissionGranted)
                }
            )
        } else {
            shouldReloadSongs(true)
        }
    }

    private val audioPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
}