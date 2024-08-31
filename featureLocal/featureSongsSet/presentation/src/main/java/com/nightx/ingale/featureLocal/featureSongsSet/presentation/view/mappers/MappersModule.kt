package com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers

import org.koin.dsl.module

internal val mappersModule = module {
    single { SongArgMapper() }
    single { SongsSetArgMapper() }
}