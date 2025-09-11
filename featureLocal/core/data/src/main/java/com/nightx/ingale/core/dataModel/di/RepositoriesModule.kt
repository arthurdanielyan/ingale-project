package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.repository.LocalMainRepositoryImpl
import com.nightx.ingale.core.dataModel.repository.SongsCacheRepositoryImpl
import com.nightx.ingale.core.domain.repository.SongsCacheRepository
import com.nightx.ingale.featureLocal.featureHome.domain.repository.LocalMainRepository
import org.koin.dsl.module

val repositoriesModule = module {
    factory<SongsCacheRepository> {
        SongsCacheRepositoryImpl(
            songsCacheDao = get()
        )
    }

    single<LocalMainRepository> {
        LocalMainRepositoryImpl(get(), get(), get(), get(), get(), get())
    }
}