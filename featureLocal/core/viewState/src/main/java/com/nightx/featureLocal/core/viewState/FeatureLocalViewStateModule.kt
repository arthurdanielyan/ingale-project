package com.nightx.featureLocal.core.viewState

import com.nightx.featureLocal.core.viewState.song.mapper.SongToViewStateMapper
import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateToSongMapper
import org.koin.dsl.module

val featureLocalViewStateModule = module {
    single { SongToViewStateMapper() }
    single { SongViewStateToSongMapper() }
}