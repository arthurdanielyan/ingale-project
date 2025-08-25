package com.nightx.ingale.root.impl.di

import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import com.nightx.ingale.root.impl.bottomNavigation.BottomBarControllerImpl
import com.nightx.ingale.root.impl.root.RootComponentFactoryImpl
import com.nightx.ingale.root.impl.snackbar.SnackbarComponentFactoryImpl
import org.koin.dsl.module

val rootModule = module {
    factory<RootComponent.Factory> {
        RootComponentFactoryImpl(
            localComponentFactory = get(),
            snackbarComponentFactory = get(),
            musicBarComponentFactory = get(),
            playbackScreenComponentFactory = get(),
            bottomBarController = get(),
        )
    }

    single<BottomBarController> {
        BottomBarControllerImpl(
            applicationScope = get(),
        )
    }

    factory<SnackbarComponent.Factory> {
        SnackbarComponentFactoryImpl(
            snackbarMessageSender = get(),
        )
    }
}