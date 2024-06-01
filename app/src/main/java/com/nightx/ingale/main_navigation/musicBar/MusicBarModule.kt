package com.nightx.ingale.main_navigation.musicBar

import com.nightx.ingale.main_navigation.musicBar.view.MusicBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val musicBarModule = module {
    viewModel {
        MusicBarViewModel(get(), get(), get())
    }
}