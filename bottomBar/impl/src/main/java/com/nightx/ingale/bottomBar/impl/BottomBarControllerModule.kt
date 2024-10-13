package com.nightx.ingale.bottomBar.impl

import com.nightx.ingale.bottomBar.api.BottomBarController
import com.nightx.ingale.bottomBar.api.BottomBarStateHolder
import org.koin.dsl.module

val bottomBarControllerModule = module {
    val bottomBarController = BottomBarControllerImpl()
    single<BottomBarController> {
        bottomBarController
    }
    single<BottomBarStateHolder> {
        bottomBarController
    }
}