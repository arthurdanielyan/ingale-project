package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.di

import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.impl.PlaybackScreenComponentFactoryImpl
import org.koin.dsl.module

internal val playbackScreenComponentFactoryModule = module {

    factory<PlaybackScreenComponent.Factory> {
        PlaybackScreenComponentFactoryImpl(
            currentSongInfoStateHolder = get(),
            playbackUserActions = get(),
            bottomBarController = get(),
        )
    }
}