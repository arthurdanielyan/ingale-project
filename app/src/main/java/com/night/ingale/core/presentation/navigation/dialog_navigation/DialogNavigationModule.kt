package com.night.ingale.core.presentation.navigation.dialog_navigation

import androidx.lifecycle.ViewModelStoreOwner
import org.koin.dsl.module

val dialogNavigationModule = module {
    single<DialogNavigatorImpl> {
        DialogNavigatorImpl()
    }
    single<DialogNavigator> {
        get<DialogNavigatorImpl>()
    }
    single<ActiveDialogHolder> {
        get<DialogNavigatorImpl>()
    }
    single<ViewModelStoreOwner> {
        get<DialogNavigatorImpl>()
    }
}