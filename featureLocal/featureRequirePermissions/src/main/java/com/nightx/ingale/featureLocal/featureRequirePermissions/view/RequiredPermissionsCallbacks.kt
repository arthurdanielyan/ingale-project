package com.nightx.ingale.featureLocal.featureRequirePermissions.view

import androidx.compose.runtime.Immutable

@Immutable
internal interface RequiredPermissionsCallbacks {

    fun onPermissionResult(permission: String, isGranted: Boolean)
    fun onOkClick(permission: String)
    fun onGoToSettingsClick(permission: String)
}