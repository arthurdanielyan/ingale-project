package com.nightx.ingale.core.utils.di

import com.nightx.ingale.core.utils.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coroutineDispatchersModule = module {
    single {
        CoroutineDispatchers(
            io = Dispatchers.IO,
            default = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }
    single {
        val dispatchers = get<CoroutineDispatchers>()
        CoroutineScope(SupervisorJob() + dispatchers.main)
    }
}