package com.nightx.ingale.main_navigation.bottom_bar_controls

import org.koin.dsl.module

val bottomBarControllerModule = module {
    val bottomBarController = BottomBarControllerImpl()
    single<BottomBarController> {
        bottomBarController
    }
    single<BottomBarEffectsHolder> {
        bottomBarController
    }
}