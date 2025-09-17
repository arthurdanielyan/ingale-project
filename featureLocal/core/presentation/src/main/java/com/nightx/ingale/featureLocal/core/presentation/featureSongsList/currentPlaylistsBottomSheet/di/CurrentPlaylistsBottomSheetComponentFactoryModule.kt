package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.di

import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.impl.CurrentPlaylistsBottomSheetComponentFactoryImpl
import org.koin.dsl.module

val currentPlaylistsBottomSheetComponentFactoryModule = module {
    factory<CurrentPlaylistsBottomSheetComponent.Factory> {
        CurrentPlaylistsBottomSheetComponentFactoryImpl(
            applicationScope = get(),
            getPlaylistsUseCase = get(),
            playlistViewStateMapper = get(),
            addSongToPlaylistUseCase = get(),
            snackbarMessageSender = get(),
            stringProvider = get(),
        )
    }
}