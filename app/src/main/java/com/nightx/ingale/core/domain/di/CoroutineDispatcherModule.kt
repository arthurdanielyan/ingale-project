package com.nightx.ingale.core.domain.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coroutineDispatcherModule = module {
    single {
        Dispatchers.Default
    }
}