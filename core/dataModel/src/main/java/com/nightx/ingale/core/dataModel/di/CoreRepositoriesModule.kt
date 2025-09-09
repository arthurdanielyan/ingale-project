package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.repository.SongsCacheRepositoryImpl
import com.nightx.ingale.core.domainModel.repository.SongsCacheRepository
import org.koin.dsl.module

val coreRepositoriesModule = module {
    factory<SongsCacheRepository> {
        SongsCacheRepositoryImpl(
            songsCacheDao = get()
        )
    }
}