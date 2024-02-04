package com.night.ingale.feature_local.main.domain

import com.night.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.night.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.night.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
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