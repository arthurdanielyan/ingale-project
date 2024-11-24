package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mappersModule = module {
    factoryOf(::SongArgMapper)
    factoryOf(::SongsSetArgMapper)
}