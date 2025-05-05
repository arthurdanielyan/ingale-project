package com.nightx.ingale.root.impl.root

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.api.root.RootComponent

internal class RootComponentFactoryImpl(
    private val localComponentFactory: LocalComponent.Factory,
    private val bottomNavigationComponentFactory: BottomNavigationComponent.Factory,
) : RootComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ) = RootComponentImpl(
        appComponentContext = appComponentContext,
        localComponentFactory = localComponentFactory,
        bottomNavigationComponentFactory = bottomNavigationComponentFactory,
    )
}