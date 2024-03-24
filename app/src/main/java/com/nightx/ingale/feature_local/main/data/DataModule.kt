package com.nightx.ingale.feature_local.main.data

import com.nightx.ingale.feature_local.main.data.repository.LocalMainRepositoryImpl
import com.nightx.ingale.feature_local.main.domain.repository.LocalMainRepository
import org.koin.dsl.module


val dataModule = module {
    single<LocalMainRepository> {
        LocalMainRepositoryImpl(get(), get(), get(), get())
    }
}