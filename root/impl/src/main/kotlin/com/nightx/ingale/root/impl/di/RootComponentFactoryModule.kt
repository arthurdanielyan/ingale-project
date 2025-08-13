package com.nightx.ingale.root.impl.di

import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import com.nightx.ingale.root.impl.bottomNavigation.BottomBarControllerImpl
import com.nightx.ingale.root.impl.root.RootComponentFactoryImpl
import com.nightx.ingale.root.impl.snackbar.SnackbarComponentFactoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val rootModule = module {
    factory<RootComponent.Factory> {
        RootComponentFactoryImpl(
            localComponentFactory = get(),
            snackbarComponentFactory = get(),
            musicBarComponentFactory = get(),
            playbackScreenComponentFactory = get(),
            bottomBarControllerImpl = get(),
        )
    }

    singleOf(::BottomBarControllerImpl) binds arrayOf(BottomBarController::class)

    factory<SnackbarComponent.Factory> {
        SnackbarComponentFactoryImpl(
            snackbarMessageSender = get(),
        )
    }
}