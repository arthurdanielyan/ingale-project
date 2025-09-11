package com.nightx.ingale.core.domain.di

import com.nightx.ingale.core.domain.usecase.RemoveSongUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreDomainModule = module {
    factoryOf(::RemoveSongUseCase)
}