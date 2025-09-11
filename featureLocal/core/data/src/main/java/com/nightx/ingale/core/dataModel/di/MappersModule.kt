package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.mapper.SongEntityMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mappersModule = module {
    factoryOf(::SongEntityMapper)
}