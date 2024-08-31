package com.nightx.ingale.core.presentation.diExt

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ViewModel> ingaleViewModels(
    savedStateHandle: SavedStateHandle,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    parameters: Array<Any> = emptyArray<Any>()
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    key = T::class.simpleName,
    parameters = {
        parametersOf(
            savedStateHandle, *parameters
        )
    }
)

@Composable
inline fun <reified T : ViewModel> ingaleViewModels(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    parameters: Array<Any> = emptyArray<Any>()
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    key = T::class.simpleName,
    parameters = {
        parametersOf(*parameters)
    }
)