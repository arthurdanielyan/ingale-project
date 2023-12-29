package com.example.ingale.feature_local.main.domain

import com.example.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.example.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.example.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetSongsUseCase(get(), get())
    }

    single {
        OrganizeSongsUseCase(get())
    }

    single {
        FilterUseCase(get())
    }
}