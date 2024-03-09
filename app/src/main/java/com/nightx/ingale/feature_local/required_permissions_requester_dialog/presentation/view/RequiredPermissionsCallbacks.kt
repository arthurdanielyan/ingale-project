package com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.view

import androidx.compose.runtime.Immutable

@Immutable
interface RequiredPermissionsCallbacks {

    fun onPermissionResult(permission: String, isGranted: Boolean)
    fun onOkClick(permission: String)
    fun onGoToSettingsClick(permission: String)
}