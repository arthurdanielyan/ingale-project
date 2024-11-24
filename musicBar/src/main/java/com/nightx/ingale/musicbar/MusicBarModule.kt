package com.nightx.ingale.musicbar

import com.nightx.ingale.musicbar.musicBarControls.MusicBarController
import com.nightx.ingale.musicbar.musicBarControls.MusicBarControllerImpl
import com.nightx.ingale.musicbar.musicBarControls.MusicBarEffectsHolder
import com.nightx.ingale.musicbar.view.MusicBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val musicBarModule = module {
    viewModelOf(::MusicBarViewModel)
    val musicBarControllerImpl = MusicBarControllerImpl()
    single<MusicBarController> {
        musicBarControllerImpl
    }
    single<MusicBarEffectsHolder> {
        musicBarControllerImpl
    }
}