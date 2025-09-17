package com.nightx.ingale.core.domain.di

import com.nightx.ingale.core.domain.playlists.usecase.AddSongToPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.CreateNewPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.core.domain.songs.usecase.RemoveSongUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreDomainModule = module {
    factoryOf(::RemoveSongUseCase)
    factoryOf(::AddSongToPlaylistUseCase)
    factoryOf(::CreateNewPlaylistUseCase)
    factoryOf(::GetPlaylistsUseCase)
}