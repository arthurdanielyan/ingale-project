package com.nightx.ingale.globalPlaybackPresentation.musicBarController

import com.nightx.ingale.globalPlaybackPresentation.view.GlobalPlaybackComponent
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val musicBarModule = module {
    singleOf(::GlobalPlaybackComponent)
    val musicBarControllerImpl = MusicBarControllerImpl()
    single<MusicBarController> {
        musicBarControllerImpl
    }
    single<MusicBarEffectsHolder> {
        musicBarControllerImpl
    }
}