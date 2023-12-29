package com.example.ingale.core.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.ingale.main_navigation.Navigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ViewModel> ingaleViewModels(
    savedStateHandle: SavedStateHandle,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    navigator: Navigator
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    parameters = {
        parametersOf(
            navigator,
            savedStateHandle
        )
    }
)

@Composable
inline fun <reified T : ViewModel> ingaleViewModels(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    navigator: Navigator
) = koinViewModel<T>(
    viewModelStoreOwner = viewModelStoreOwner,
    parameters = {
        parametersOf(
            navigator
        )
    }
)