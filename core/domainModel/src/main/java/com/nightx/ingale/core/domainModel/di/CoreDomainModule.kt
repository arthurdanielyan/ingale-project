package com.nightx.ingale.core.domainModel.di

import com.nightx.ingale.core.domainModel.usecase.RemoveSongUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreDomainModule = module {
    factoryOf(::RemoveSongUseCase)
}