package com.nightx.ingale.feature_local.main.presentation

import com.nightx.ingale.feature_local.main.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.feature_local.main.presentation.view.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainViewModel
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
}