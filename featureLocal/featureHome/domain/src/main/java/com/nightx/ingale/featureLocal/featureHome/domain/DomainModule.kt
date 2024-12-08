package com.nightx.ingale.featureLocal.featureHome.domain

import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetCachedState
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetSongsUseCase)
    factoryOf(::OrganizeSongsUseCase)
    factoryOf(::GetCachedState)
}