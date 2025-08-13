package com.nightx.ingale.featureLocal.featureRequirePermissions.di

import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import com.nightx.ingale.featureLocal.featureRequirePermissions.impl.RequirePermissionsComponentFactoryImpl
import org.koin.dsl.module

val requirePermissionsComponentFactoryModule = module {

    factory<RequirePermissionsComponent.Factory> {
        RequirePermissionsComponentFactoryImpl(
            settingsLauncher = get(),
            permissionInspector = get(),
        )
    }
}