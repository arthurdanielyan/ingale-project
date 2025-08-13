package com.nightx.ingale.core.audioPlayer.impl

import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val playerServiceCommunicatorModule = module {
    singleOf(::PlayerServiceCommunicator) {
        createdAtStart()
    }
    single<CurrentSongInfoStateProvider> {
        get<PlayerServiceCommunicator>()
    }
    single<PlaybackUserActions> {
        get<PlayerServiceCommunicator>()
    }
}