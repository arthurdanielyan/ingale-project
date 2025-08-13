package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.impl

import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent

internal class PlaybackScreenComponentFactoryImpl(
    private val currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playbackUserActions: PlaybackUserActions,
    private val bottomBarController: BottomBarController,
) : PlaybackScreenComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        onClose: () -> Unit,
    ): PlaybackScreenComponent {
        return PlaybackScreenComponentImpl(
            appComponentContext = appComponentContext,
            onCloseCallback = onClose,
            currentSongInfoStateHolder = currentSongInfoStateHolder,
            playbackUserActions = playbackUserActions,
            bottomBarController = bottomBarController,
        )
    }
}