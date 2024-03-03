package com.nightx.ingale.main_navigation.bottom_bar_controls

import org.koin.dsl.module

val bottomBarControllerModule = module {
    single<BottomBarControllerImpl> {
        BottomBarControllerImpl()
    }
    single<BottomBarController> {
        get<BottomBarControllerImpl>()
    }
    single<BottomBarEffectsHolder> {
        get<BottomBarControllerImpl>()
    }
}