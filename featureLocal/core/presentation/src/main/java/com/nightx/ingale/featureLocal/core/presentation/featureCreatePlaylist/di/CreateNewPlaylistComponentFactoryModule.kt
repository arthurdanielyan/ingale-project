package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.di

import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.impl.CreatePlaylistDialogComponentFactoryImpl
import org.koin.dsl.module

val createNewPlaylistComponentFactoryModule = module {
    factory<CreatePlaylistDialogComponent.Factory> {
        CreatePlaylistDialogComponentFactoryImpl(
            getPlaylistsUseCase = get(),
            createNewPlaylistUseCase = get(),
            applicationScope = get(),
            snackbarMessageSender = get(),
            stringProvider = get(),
        )
    }
}