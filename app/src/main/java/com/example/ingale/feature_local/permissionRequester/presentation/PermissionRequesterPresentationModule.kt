package com.example.ingale.feature_local.permissionRequester.presentation

import com.example.ingale.feature_local.permissionRequester.presentation.view.PermissionRequesterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        PermissionRequesterViewModel(get())
    }
}