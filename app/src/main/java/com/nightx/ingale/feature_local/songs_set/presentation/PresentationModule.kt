package com.nightx.ingale.feature_local.songs_set.presentation

import com.nightx.ingale.core.di.viewModelWithSavedStateHandle
import com.nightx.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        SongsSetViewModel(savedStateHandle)
    }
}