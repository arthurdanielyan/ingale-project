package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mappersModule = module {
    factoryOf(::SongsSetViewStateMapper)
    factoryOf(::SongsSetToNavArgMapper)
}