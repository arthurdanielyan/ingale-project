package com.nightx.ingale.featureLocal.core.ui

import com.nightx.ingale.featureLocal.core.ui.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.core.ui.viewState.mapper.SongViewStateToSongMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureLocalCoreUiModule = module {
    factoryOf(::SongViewStateMapper)
    factoryOf(::SongViewStateToSongMapper)
}