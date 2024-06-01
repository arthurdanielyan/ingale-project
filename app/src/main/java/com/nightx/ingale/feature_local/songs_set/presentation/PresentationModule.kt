package com.nightx.ingale.feature_local.songs_set.presentation

import com.nightx.ingale.core.presentation.di.viewModelWithSavedStateHandle
import com.nightx.ingale.feature_local.songs_set.presentation.view.mappers.SongsSetNavArgMapper
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.SongsSetViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        SongsSetViewModel(savedStateHandle, get(), get())
    }
    single {
        SongsSetNavArgMapper()
    }
}