package com.nightx.ingale.featureLocal.featureHome.domain

import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetSongsUseCase(get()) }
    single { OrganizeSongsUseCase(get()) }
}