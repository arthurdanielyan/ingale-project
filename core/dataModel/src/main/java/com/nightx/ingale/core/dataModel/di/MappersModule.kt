package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.mapper.SongRealmMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mappersModule = module {
    factoryOf(::SongRealmMapper)
}