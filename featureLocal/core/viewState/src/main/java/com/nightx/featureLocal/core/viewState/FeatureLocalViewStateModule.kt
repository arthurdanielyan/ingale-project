package com.nightx.featureLocal.core.viewState

import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateMapper
import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateToSongMapper
import org.koin.dsl.module

val featureLocalViewStateModule = module {
    single { SongViewStateMapper(get()) }
    single { SongViewStateToSongMapper() }
}