package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import org.koin.dsl.module

internal val mappersModule = module {
    single { SongsSetViewStateMapper(get(), get()) }
    single { SongsSetToNavArgMapper() }
}