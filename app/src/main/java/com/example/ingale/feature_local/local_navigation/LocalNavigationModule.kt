package com.example.ingale.feature_local.local_navigation

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