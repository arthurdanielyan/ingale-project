package com.nightx.ingale.core.presentation.osExt.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.nightx.ingale.core.presentation.osExt.api.PermissionInspector

internal class PermissionInspectorImpl(
    private val applicationContext: Context,
) : PermissionInspector {

    override val appRequiredPermissions =
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_AUDIO)
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
            )
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    override val audioPermissionKey =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    override fun isAudioPermissionGranted(): Boolean {
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

    override fun isPermissionDenied(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            permission
        ) == PackageManager.PERMISSION_DENIED
    }
}