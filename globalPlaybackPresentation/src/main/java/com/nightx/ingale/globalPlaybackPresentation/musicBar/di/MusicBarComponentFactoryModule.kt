package com.nightx.ingale.globalPlaybackPresentation.musicBar.di

import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.globalPlaybackPresentation.musicBar.impl.MusicBarComponentFactoryImpl
import org.koin.dsl.module

val musicBarComponentFactoryModule = module {

    factory<MusicBarComponent.Factory> {
        MusicBarComponentFactoryImpl(
            currentSongInfoStateHolder = get(),
            playbackUserActions = get(),
            musicBarEffectsHolder = get(),
        )
    }
}