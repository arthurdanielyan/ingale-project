package com.night.ingale.feature_local.main.presentation

import com.night.ingale.feature_local.main.presentation.view.LocalMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        LocalMainViewModel(get(), get(), get(), get(), get(), get())
    }
}