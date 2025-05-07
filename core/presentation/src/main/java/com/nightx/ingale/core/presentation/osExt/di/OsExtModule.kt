package com.nightx.ingale.core.presentation.osExt.di

import com.nightx.ingale.core.presentation.osExt.api.PermissionInspector
import com.nightx.ingale.core.presentation.osExt.api.SettingsLauncher
import com.nightx.ingale.core.presentation.osExt.impl.PermissionInspectorImpl
import com.nightx.ingale.core.presentation.osExt.impl.SettingsLauncherImpl
import org.koin.dsl.module

val osExtModule = module {
    single<SettingsLauncher> {
        SettingsLauncherImpl(
            applicationContext = get(),
        )
    }

    single<PermissionInspector> {
        PermissionInspectorImpl(
            applicationContext = get(),
        )
    }
}