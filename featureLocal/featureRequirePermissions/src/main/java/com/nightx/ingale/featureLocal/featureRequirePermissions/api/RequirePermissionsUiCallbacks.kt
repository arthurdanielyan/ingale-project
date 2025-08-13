package com.nightx.ingale.featureLocal.featureRequirePermissions.api

import androidx.compose.runtime.Immutable

@Immutable
interface RequirePermissionsUiCallbacks {

    fun onPermissionResult(permission: String, isGranted: Boolean)
    fun onOkClick(permission: String)
    fun onGoToSettingsClick(permission: String)
}