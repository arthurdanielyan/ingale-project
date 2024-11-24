package com.nightx.featureLocal.core.viewState

import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateMapper
import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateToSongMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureLocalViewStateModule = module {
    factoryOf(::SongViewStateMapper)
    factoryOf(::SongViewStateToSongMapper)
}