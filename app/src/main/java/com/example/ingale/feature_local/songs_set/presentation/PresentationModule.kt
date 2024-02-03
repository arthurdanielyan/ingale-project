package com.example.ingale.feature_local.songs_set.presentation

import com.example.ingale.core.di.viewModelWithSavedStateHandle
import com.example.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        SongsSetViewModel(savedStateHandle)
    }
}