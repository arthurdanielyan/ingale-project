package com.nightx.ingale.core.audioPlayer.impl

import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import org.koin.dsl.module

val audioPlayerModule = module {
    single(createdAtStart = true) {
        AudioPlayer(get(), get())
    }
    single<CurrentSongInfoStateProvider> {
        get<AudioPlayer>()
    }
    single<PlayerUiActions> {
        get<AudioPlayer>()
    }
}