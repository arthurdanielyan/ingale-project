package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.di

import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.impl.SongsListComponentFactoryImpl
import org.koin.dsl.module

val songsListComponentFactoryModule = module {
    factory<SongsListComponent.Factory> {
        SongsListComponentFactoryImpl(
            songOperationsParentComponentFactory = get(),
            songViewStateToDomainMapper = get(),
            playbackUserActions = get(),
        )
    }
}
