package com.nightx.ingale.core.domain.di

import com.nightx.ingale.core.domain.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coroutineDispatcherModule = module {
    single {
        CoroutineDispatchers(
            io = Dispatchers.IO,
            default = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }
}