package com.nightx.ingale.featureLocal.featureHome.presentation

import com.nightx.ingale.featureLocal.featureHome.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.LocalMainViewModel
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.mappersModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LocalMainViewModel)
    single {
        RequiredPermissionsInspector(get(), get())
    }
    factoryOf(::SongsSetToNavArgMapper)
} + mappersModule