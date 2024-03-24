package com.nightx.ingale.core.data.di

import com.nightx.ingale.core.data.model.mapper.SongMapper
import org.koin.dsl.module

val mappersModule = module {
    single { SongMapper() }
}