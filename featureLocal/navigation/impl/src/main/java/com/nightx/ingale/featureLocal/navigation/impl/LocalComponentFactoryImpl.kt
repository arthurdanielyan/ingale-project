package com.nightx.ingale.featureLocal.navigation.impl

import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent

internal class LocalComponentFactoryImpl(
    private val bottomBarController: BottomBarController,
    private val localHomeComponentFactory: LocalHomeComponent.Factory,
    private val songsSetComponentFactory: SongsSetComponent.Factory,
) : LocalComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ): LocalComponent {
        return LocalComponentImpl(
            appComponentContext = appComponentContext,
            bottomBarController = bottomBarController,
            localHomeComponentFactory = localHomeComponentFactory,
            songsSetComponentFactory = songsSetComponentFactory,
        )
    }
}