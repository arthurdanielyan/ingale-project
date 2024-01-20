package com.example.ingale.feature_local.songs_set.presentation

import com.example.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {parameters ->
        SongsSetViewModel(parameters[0])
    }
}