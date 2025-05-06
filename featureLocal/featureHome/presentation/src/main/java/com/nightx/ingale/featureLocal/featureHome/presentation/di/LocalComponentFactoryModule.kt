package com.nightx.ingale.featureLocal.featureHome.presentation.di

import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.LocalHomeComponentFactoryImpl
import org.koin.dsl.module

val localHomeComponentFactoryModule = module {
    factory<LocalHomeComponent.Factory> {
        LocalHomeComponentFactoryImpl(
            getSongsUseCase = get(),
            getCachedState = get(),
            songViewStateMapper = get(),
            songsSetViewStateMapper = get(),
            organizeSongsUseCase = get(),
            requiredPermissionsInspector = get(),
            songsSetToNavArgMapper = get(),
            applicationContext = get(),
            playerUiActions = get(),
            snackbarMessageSender = get(),
            stringProvider = get(),
        )
    }
}