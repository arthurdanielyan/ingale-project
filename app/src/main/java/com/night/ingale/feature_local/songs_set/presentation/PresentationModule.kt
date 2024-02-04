package com.night.ingale.feature_local.songs_set.presentation

import com.night.ingale.core.di.viewModelWithSavedStateHandle
import com.night.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        SongsSetViewModel(savedStateHandle)
    }
}