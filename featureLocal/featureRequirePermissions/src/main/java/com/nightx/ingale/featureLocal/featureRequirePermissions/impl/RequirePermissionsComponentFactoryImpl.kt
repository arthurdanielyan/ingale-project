package com.nightx.ingale.featureLocal.featureRequirePermissions.impl

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.presentation.osExt.api.PermissionInspector
import com.nightx.ingale.core.presentation.osExt.api.SettingsLauncher
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent.Params

class RequirePermissionsComponentFactoryImpl(
    private val settingsLauncher: SettingsLauncher,
    private val permissionInspector: PermissionInspector
) : RequirePermissionsComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        params: Params,
        onAudioPermissionResult: (Boolean) -> Unit,
    ): RequirePermissionsComponent {
        return RequirePermissionsComponentImpl(
            appComponentContext = appComponentContext,
            params = params,
            onAudioPermissionResult = onAudioPermissionResult,
            settingsLauncher = settingsLauncher,
            permissionInspector = permissionInspector,
        )
    }
}