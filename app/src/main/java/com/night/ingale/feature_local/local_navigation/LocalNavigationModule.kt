package com.night.ingale.feature_local.local_navigation

import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalNavigatorEventsHolder
import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalNavigatorImpl
import org.koin.dsl.module

val localNavigationModule = module {
    single<LocalNavigatorImpl> {
        LocalNavigatorImpl()
    }
    single<LocalNavigatorEventsHolder> {
        get<LocalNavigatorImpl>()
    }
    single<LocalNavigator> {
        get<LocalNavigatorImpl>()
    }
}