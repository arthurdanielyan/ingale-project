package com.nightx.ingale.featureLocal.featureSongsSet.presentation

import com.nightx.ingale.core.presentation.diExt.viewModelWithSavedStateHandle
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.mappers.mappersModule
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel.SongsSetViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        SongsSetViewModel(savedStateHandle, get(), get(), get(), get())
    }
} + mappersModule