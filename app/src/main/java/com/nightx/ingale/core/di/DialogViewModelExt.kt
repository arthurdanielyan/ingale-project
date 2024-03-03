package com.nightx.ingale.core.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.LocalDialogViewModelStoreOwner
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ViewModel> ingaleDialogViewModels(
    savedStateHandle: SavedStateHandle,
    viewModelStoreOwner: ViewModelStoreOwner = LocalDialogViewModelStoreOwner.current
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    key = T::class.simpleName,
    parameters = {
        parametersOf(
            savedStateHandle
        )
    }
)

@Composable
inline fun <reified T : ViewModel> ingaleDialogViewModels(
    viewModelStoreOwner: ViewModelStoreOwner = LocalDialogViewModelStoreOwner.current
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    key = T::class.simpleName
)