package com.nightx.ingale.featureLocal.featureHome.presentation.di

import com.nightx.ingale.featureLocal.featureHome.presentation.impl.RequiredPermissionsInspector
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureHomePresentationModule = module {
    single {
        RequiredPermissionsInspector(get(), get())
    }
    factoryOf(::SongsSetToNavArgMapper)
} + mappersModule + localHomeComponentFactoryModule