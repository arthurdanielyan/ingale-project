package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.impl

import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.nightx.ingale.bottomBarApi.BottomBarController
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenUiCallbacks
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenViewState
import kotlinx.coroutines.flow.map

internal class PlaybackScreenComponentImpl(
    appComponentContext: AppComponentContext,
    private val onCloseCallback: () -> Unit,
    currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playbackUserActions: PlaybackUserActions,
    private val bottomBarController: BottomBarController,
) : PlaybackScreenComponent,
    PlaybackScreenUiCallbacks,
    AppComponentContext by appComponentContext {

    private val backCallback = BackCallback {
        onCloseCallback()
    }

    override val uiState = currentSongInfoStateHolder.currentSongInfo.map { currentSongInfo ->
        PlaybackScreenViewState(
            currentSongPreviewPath = currentSongInfo?.currentSongPreviewPath.orEmpty(),
            isPlaying = currentSongInfo?.isPlaying ?: false,
            songName = currentSongInfo?.songName.orEmpty(),
            artistName = currentSongInfo?.artistName.orEmpty(),
            seekPercentage = currentSongInfo?.seekPercentage ?: 0f,
        )
    }.stateInWhileSubscribed(componentScope, PlaybackScreenViewState())

    override val playbackProgress = currentSongInfoStateHolder.currentSongInfo.map {
        it?.seekPercentage ?: 0f
    }.stateInWhileSubscribed(componentScope, 0f)

    override val uiCallbacks = this

    init {
        backHandler.register(backCallback)
        val bottomBarInitialVisibility = bottomBarController.isBottomBarVisible.value
        doOnStart {
            bottomBarController.setVisibility(false)
        }
        doOnStop {
            bottomBarController.setVisibility(bottomBarInitialVisibility)
        }
    }

    override fun onTogglePlayback() {
        playbackUserActions.togglePlaying()
    }

    override fun onSkipToNextClick() {
        playbackUserActions.skipToNext()
    }

    override fun onSkipToPreviousClick() {
        playbackUserActions.skipToPrevious()
    }

    override fun onSeekTo(percentage: Float) {
        playbackUserActions.seekTo(percentage)
    }

    override fun onClose() {
        onCloseCallback()
    }
}