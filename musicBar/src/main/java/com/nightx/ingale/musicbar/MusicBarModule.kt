package com.nightx.ingale.musicbar

import com.nightx.ingale.musicbar.musicBarControls.MusicBarController
import com.nightx.ingale.musicbar.musicBarControls.MusicBarControllerImpl
import com.nightx.ingale.musicbar.view.MusicBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val musicBarModule = module {
    viewModel {
        MusicBarViewModel(get(), get(), get())
    }
    single<MusicBarController> {
        MusicBarControllerImpl()
    }
}