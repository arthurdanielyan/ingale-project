package com.example.ingale.feature_local.main.presentation

import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { parameters ->
        LocalMainViewModel(parameters[0], get(), get(), get())
    }
}