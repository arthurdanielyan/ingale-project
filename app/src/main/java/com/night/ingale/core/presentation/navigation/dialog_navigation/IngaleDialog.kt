package com.night.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.compose.koinInject

class DialogViewModelStoreOwner : ViewModelStoreOwner {

    override val viewModelStore = ViewModelStore()
}

@Composable
fun IngaleDialogBehaviour(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalDialogViewModelStoreOwner provides koinInject<DialogViewModelStore>().currentViewModelStore
    ) {
        content()
    }
}