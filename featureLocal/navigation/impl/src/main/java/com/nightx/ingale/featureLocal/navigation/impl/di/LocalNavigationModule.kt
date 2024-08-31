package com.nightx.ingale.featureLocal.navigation.impl.di

import com.nightx.ingale.featureLocal.navigation.api.LocalNavigator
import com.nightx.ingale.featureLocal.navigation.impl.LocalNavigatorImpl
import org.koin.dsl.module

val localNavigationModule = module {
    single<LocalNavigator> {
        LocalNavigatorImpl()
    }
}