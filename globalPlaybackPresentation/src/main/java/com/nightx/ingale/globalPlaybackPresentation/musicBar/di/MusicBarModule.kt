package com.nightx.ingale.globalPlaybackPresentation.musicBar.di

import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarController
import com.nightx.ingale.globalPlaybackPresentation.musicBar.impl.MusicBarControllerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

internal val musicBarModule = module {
    singleOf(::MusicBarControllerImpl) binds arrayOf(MusicBarController::class)
}