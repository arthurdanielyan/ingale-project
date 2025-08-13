package com.nightx.ingale.globalPlaybackPresentation.di

import com.nightx.ingale.globalPlaybackPresentation.musicBar.di.musicBarComponentFactoryModule
import com.nightx.ingale.globalPlaybackPresentation.musicBar.di.musicBarControllerModule
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.di.playbackScreenComponentFactoryModule

val globalPlaybackPresentationModule = musicBarControllerModule + musicBarComponentFactoryModule +
        playbackScreenComponentFactoryModule