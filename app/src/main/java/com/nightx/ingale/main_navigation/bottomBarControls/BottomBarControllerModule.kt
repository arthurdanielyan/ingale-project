package com.nightx.ingale.main_navigation.bottomBarControls

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