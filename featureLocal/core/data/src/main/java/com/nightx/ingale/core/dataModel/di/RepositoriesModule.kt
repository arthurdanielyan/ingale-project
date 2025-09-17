package com.nightx.ingale.core.dataModel.di

import com.nightx.ingale.core.dataModel.repository.PlaylistsRepositoryImpl
import com.nightx.ingale.core.dataModel.repository.SongsCacheRepositoryImpl
import com.nightx.ingale.core.domain.playlists.repository.PlaylistsRepository
import com.nightx.ingale.core.domain.songs.repository.SongsCacheRepository
import org.koin.dsl.module

val repositoriesModule = module {
    factory<SongsCacheRepository> {
        SongsCacheRepositoryImpl(
            songsCacheDao = get(),
            applicationContext = get(),
            dispatchers = get(),
            songEntityMapper = get(),
            stringProvider = get(),
            applicationScope = get(),
        )
    }

    factory<PlaylistsRepository> {
        PlaylistsRepositoryImpl(
            playlistsDao = get(),
            playlistWithSongsMapper = get(),
        )
    }
}