package com.nightx.ingale.core.audio_player

import com.nightx.ingale.core.audio_player.actions.PlayerUiActions
import org.koin.dsl.module

val audioPlayerModule = module {
    single(createdAtStart = true) {
        AudioPlayer(get())
    }
    single<CurrentSongInfoStateHolder> {
        get<AudioPlayer>()
    }
    single<PlayerUiActions> {
        get<AudioPlayer>()
    }
}