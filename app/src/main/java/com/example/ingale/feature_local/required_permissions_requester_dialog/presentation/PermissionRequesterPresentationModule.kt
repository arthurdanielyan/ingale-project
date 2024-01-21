package com.example.ingale.feature_local.required_permissions_requester_dialog.presentation

import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { parameters ->
        RequiredPermissionsRequesterViewModel(parameters[0], get())
    }
}