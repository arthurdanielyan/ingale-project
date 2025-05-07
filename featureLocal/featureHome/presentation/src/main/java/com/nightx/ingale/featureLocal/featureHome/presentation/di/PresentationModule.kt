package com.nightx.ingale.featureLocal.featureHome.presentation.di

import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureHomePresentationModule = module {
    factoryOf(::SongsSetToNavArgMapper)
} + mappersModule + localHomeComponentFactoryModule