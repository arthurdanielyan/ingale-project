package com.nightx.ingale.core.audioPlayer.impl

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val audioPlayerImplModule = module {
    factoryOf(::SongNotFoundHandler)
}