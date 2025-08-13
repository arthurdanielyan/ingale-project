package com.nightx.ingale.globalPlaybackPresentation.musicBar.impl

import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent

internal class MusicBarComponentFactoryImpl(
    private val currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playbackUserActions: PlaybackUserActions,
    private val musicBarEffectsHolder: MusicBarControllerImpl,
) : MusicBarComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        onMusicBarClicked: () -> Unit,
    ): MusicBarComponent {
        return MusicBarComponentImpl(
            appComponentContext = appComponentContext,
            currentSongInfoStateHolder = currentSongInfoStateHolder,
            onMusicBarClicked = onMusicBarClicked,
            playbackUserActions = playbackUserActions,
            musicBarEffectsHolder = musicBarEffectsHolder,
        )
    }
}