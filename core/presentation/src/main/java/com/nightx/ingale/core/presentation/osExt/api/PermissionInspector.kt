package com.nightx.ingale.core.presentation.osExt.api

interface PermissionInspector {

    val appRequiredPermissions: List<String>

    val audioPermissionKey: String

    fun isAudioPermissionGranted(): Boolean

    fun isPermissionDenied(permission: String): Boolean
}