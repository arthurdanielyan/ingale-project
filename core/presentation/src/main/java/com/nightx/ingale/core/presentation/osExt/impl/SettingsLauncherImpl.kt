package com.nightx.ingale.core.presentation.osExt.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.nightx.ingale.core.presentation.osExt.api.SettingsLauncher

internal class SettingsLauncherImpl(
    private val applicationContext: Context
) : SettingsLauncher {

    override fun goToAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationContext.packageName, null)
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(this)
        }
    }
}