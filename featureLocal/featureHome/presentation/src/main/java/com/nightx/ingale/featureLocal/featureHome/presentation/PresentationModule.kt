package com.nightx.ingale.featureLocal.featureHome.presentation

import com.nightx.ingale.featureLocal.featureHome.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.LocalMainViewModel
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.mappersModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        LocalMainViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    single {
        RequiredPermissionsInspector(get(), get())
    }
    single {
        SongsSetToNavArgMapper()
    }
} + mappersModule