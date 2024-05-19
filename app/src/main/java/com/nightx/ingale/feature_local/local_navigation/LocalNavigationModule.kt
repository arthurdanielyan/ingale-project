package com.nightx.ingale.feature_local.local_navigation

import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalNavigatorImpl
import org.koin.dsl.module

val localNavigationModule = module {
    single<LocalNavigator> {
        LocalNavigatorImpl()
    }
}