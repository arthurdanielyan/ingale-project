package com.nightx.ingale.featureLocal.featureHome.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.nightx.ingale.core.presentation.dialogComponent.dialogComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.view.RequiredPermissionsRequesterDialogComponent
import kotlinx.coroutines.CoroutineScope


internal class RequiredPermissionsInspector(
    private val applicationContext: Context,
    private val scope: CoroutineScope
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

    private val requiredPermissionsRequesterDialogComponent by dialogComponent(scope) {
        RequiredPermissionsRequesterDialogComponent(
            applicationContext = applicationContext
        )
    }

    fun start(shouldReloadSongs: (Boolean) -> Unit) {
        checkPermissions(shouldReloadSongs)
        subscribeDialogCallbacks(shouldReloadSongs)
    }

    private fun checkPermissions(shouldReloadSongs: (Boolean) -> Unit) {
        val permissionsToRequest = requiredPermissions.filter { requiredPermission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                requiredPermission
            ) == PackageManager.PERMISSION_DENIED
        }
        if (permissionsToRequest.isNotEmpty()) {
            requiredPermissionsRequesterDialogComponent.show()
        } else {
            shouldReloadSongs(true)
        }
    }

    private fun subscribeDialogCallbacks(shouldReloadSongs: (Boolean) -> Unit) {
        requiredPermissionsRequesterDialogComponent
            .subscribeToVmCallbacks(scope, shouldReloadSongs)
    }
}