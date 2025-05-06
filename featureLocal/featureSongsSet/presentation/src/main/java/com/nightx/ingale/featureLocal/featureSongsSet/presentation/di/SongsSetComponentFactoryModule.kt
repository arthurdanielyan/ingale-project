package com.nightx.ingale.featureLocal.featureSongsSet.presentation.di

import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.SongsSetComponentFactoryImpl
import org.koin.dsl.module

internal val songsSetComponentFactoryModule = module {
    factory<SongsSetComponent.Factory> {
        SongsSetComponentFactoryImpl(
            songArgMapper = get(),
            songsSetParamsMapper = get(),
            playerUiActions = get(),
        )
    }
}