package com.nightx.ingale.feature_local.local_navigation

import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import org.koin.dsl.module

val localNavigationModule = module {
    single<LocalNavigator> {
        LocalNavigator()
    }
}