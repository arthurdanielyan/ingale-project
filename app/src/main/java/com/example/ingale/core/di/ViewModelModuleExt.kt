package com.example.ingale.core.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.scope.Scope

inline fun <reified VM : ViewModel> Module.viewModelWithSavedStateHandle(
    crossinline factory: Scope.(SavedStateHandle) -> VM
) {
    this.viewModel { parameters ->
        factory(parameters[0])
    }
}