package com.nightx.ingale.featureLocal.navigation.impl.di

import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.featureLocal.navigation.impl.LocalComponentFactoryImpl
import org.koin.dsl.module

val localComponentFactoryModule = module {
    single<LocalComponent.Factory> {
        LocalComponentFactoryImpl(
            bottomBarController = get(),
            localHomeComponentFactory = get(),
            songsSetComponentFactory = get(),
        )
    }
}