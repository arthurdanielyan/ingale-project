package com.example.ingale.feature_local.required_permissions_requester_dialog.presentation

import com.example.ingale.core.di.viewModelWithSavedStateHandle
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelWithSavedStateHandle { savedStateHandle ->
        RequiredPermissionsRequesterViewModel(savedStateHandle, get(), get())
    }
}