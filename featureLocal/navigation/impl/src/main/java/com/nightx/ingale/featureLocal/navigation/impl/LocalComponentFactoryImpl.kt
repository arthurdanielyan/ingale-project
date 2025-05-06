package com.nightx.ingale.featureLocal.navigation.impl

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent

internal class LocalComponentFactoryImpl(
    private val localHomeComponentFactory: LocalHomeComponent.Factory
) : LocalComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ): LocalComponent {
        return LocalComponentImpl(
            appComponentContext = appComponentContext,
            localHomeComponentFactory = localHomeComponentFactory,
        )
    }
}