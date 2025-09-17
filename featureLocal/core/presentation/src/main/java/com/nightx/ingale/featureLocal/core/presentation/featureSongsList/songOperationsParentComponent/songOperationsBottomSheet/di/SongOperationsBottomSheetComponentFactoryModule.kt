package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.di

import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.impl.SongOperationsBottomSheetComponentFactoryImpl
import org.koin.dsl.module

val songOperationsBottomSheetComponentFactoryModule = module {
    factory<SongOperationsBottomSheetComponent.Factory> {
        SongOperationsBottomSheetComponentFactoryImpl()
    }
}