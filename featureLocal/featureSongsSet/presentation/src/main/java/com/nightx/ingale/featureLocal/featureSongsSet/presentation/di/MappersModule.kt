package com.nightx.ingale.featureLocal.featureSongsSet.presentation.di

import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongArgMapper
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongsSetParamsMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mappersModule = module {
    factoryOf(::SongArgMapper)
    factoryOf(::SongsSetParamsMapper)
}