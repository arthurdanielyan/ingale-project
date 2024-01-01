package com.example.ingale.feature_local.main.data

import com.example.ingale.feature_local.main.data.repository.LocalMainRepositoryImpl
import com.example.ingale.feature_local.main.domain.repository.LocalMainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module


val dataModule = module {
    single<LocalMainRepository> {
        LocalMainRepositoryImpl(get())
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindLocalMainRepository(impl: LocalMainRepositoryImpl): LocalMainRepository
}
