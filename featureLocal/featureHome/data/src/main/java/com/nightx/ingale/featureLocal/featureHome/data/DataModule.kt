package com.nightx.ingale.featureLocal.featureHome.data

import com.nightx.ingale.featureLocal.featureHome.data.repository.LocalMainRepositoryImpl
import com.nightx.ingale.featureLocal.featureHome.domain.repository.LocalMainRepository
import org.koin.dsl.module

val dataModule = module {
    single<LocalMainRepository> {
        LocalMainRepositoryImpl(get(), get(), get(), get(), get())
    }
}