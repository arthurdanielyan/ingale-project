package com.nightx.ingale.featureLocal.core.presentation.common

import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.PlaylistViewStateMapper
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.SongViewStateToDomainMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureLocalCommonUiModule = module {
    factoryOf(::SongViewStateMapper)
    factoryOf(::SongViewStateToDomainMapper)
    factoryOf(::PlaylistViewStateMapper)
}