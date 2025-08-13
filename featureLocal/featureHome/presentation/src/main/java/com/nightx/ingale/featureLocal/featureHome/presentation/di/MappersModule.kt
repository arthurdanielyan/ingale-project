package com.nightx.ingale.featureLocal.featureHome.presentation.di

import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetViewStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mappersModule = module {
    factoryOf(::SongsSetViewStateMapper)
    factoryOf(::SongsSetToNavArgMapper)
}