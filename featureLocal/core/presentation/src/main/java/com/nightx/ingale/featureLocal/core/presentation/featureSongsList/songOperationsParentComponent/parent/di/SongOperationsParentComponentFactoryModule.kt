package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.di

import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.impl.SongOperationsParentComponentFactoryImpl
import org.koin.dsl.module

val songOperationsParentComponentFactoryModule = module {
    factory<SongOperationsParentComponent.Factory> {
        SongOperationsParentComponentFactoryImpl(
            songOperationsBottomSheetComponentFactory = get(),
            createPlaylistDialogComponentFactory = get(),
            currentPlaylistsBottomSheetComponentFactory = get(),
            getPlaylistsUseCase = get(),
        )
    }
}