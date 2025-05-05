package com.nightx.ingale.root.impl.di

import com.nightx.ingale.bottomBar.api.BottomBarController
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.impl.bottomNavigation.BottomBarControllerImpl
import com.nightx.ingale.root.impl.bottomNavigation.BottomNavigationComponentFactory
import com.nightx.ingale.root.impl.root.RootComponentFactoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val rootModule = module {
    singleOf<
            RootComponent.Factory,
            LocalComponent.Factory,
            BottomNavigationComponent.Factory,
            >(::RootComponentFactoryImpl)

    factoryOf<
            BottomNavigationComponent.Factory,
            BottomBarControllerImpl
            >(::BottomNavigationComponentFactory)

    singleOf(::BottomBarControllerImpl) binds arrayOf(BottomBarController::class)
}