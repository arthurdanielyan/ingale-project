package com.nightx.ingale.feature_local.main.domain

import com.nightx.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetSongsUseCase(get())
    }

    single {
        OrganizeSongsUseCase(get())
    }

    single {
        FilterUseCase(get())
    }
}