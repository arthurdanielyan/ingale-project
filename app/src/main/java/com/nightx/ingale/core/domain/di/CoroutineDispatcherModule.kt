package com.nightx.ingale.core.domain.di

import com.nightx.ingale.core.domain.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coroutinesModule = module {
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