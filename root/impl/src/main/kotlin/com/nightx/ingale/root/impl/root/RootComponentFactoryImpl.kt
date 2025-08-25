package com.nightx.ingale.root.impl.root

import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.snackbar.SnackbarComponent

internal class RootComponentFactoryImpl(
    private val localComponentFactory: LocalComponent.Factory,
    private val snackbarComponentFactory: SnackbarComponent.Factory,
    private val musicBarComponentFactory: MusicBarComponent.Factory,
    private val playbackScreenComponentFactory: PlaybackScreenComponent.Factory,
    private val bottomBarController: BottomBarController,
) : RootComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ) = RootComponentImpl(
        appComponentContext = appComponentContext,
        localComponentFactory = localComponentFactory,
        snackbarComponentFactory = snackbarComponentFactory,
        musicBarComponentFactory = musicBarComponentFactory,
        playbackScreenComponentFactory = playbackScreenComponentFactory,
        bottomBarController = bottomBarController,
    )
}