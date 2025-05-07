package com.nightx.ingale.globalPlaybackPresentation.musicBar.impl

import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarEffect
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarUiCallbacks
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MusicBarComponentImpl(
    appComponentContext: AppComponentContext,
    currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playbackUserActions: PlaybackUserActions,
    private val musicBarEffectsHolder: MusicBarControllerImpl,
) : MusicBarComponent,
    MusicBarUiCallbacks,
    AppComponentContext by appComponentContext {

    private val isMusicBarExpanded = MutableStateFlow(true)

    override val uiState = combine(
        currentSongInfoStateHolder.currentSongInfo,
        isMusicBarExpanded,
    ) { currentSongInfo, isMusicBarExpanded ->
        MusicBarViewState(
            currentSongPreviewPath = currentSongInfo?.currentSongPreviewPath,
            isPlaying = currentSongInfo?.isPlaying ?: false,
            songName = currentSongInfo?.songName.orEmpty(),
            artistName = currentSongInfo?.artistName.orEmpty(),
            isExpanded = isMusicBarExpanded,
            isMusicBarVisible = currentSongInfo != null,
        )
    }.stateInWhileSubscribed(componentScope, MusicBarViewState())

    override val uiCallbacks = this

    init {
        observeMusicBarEffects()
    }

    private fun observeMusicBarEffects() {
        componentScope.launch {
            musicBarEffectsHolder.musicBarEffect.collectLatest { musicBarEffect ->
                if (uiState.value.isMusicBarVisible) {
                    when (musicBarEffect) {
                        MusicBarEffect.ExpandMusicBar -> {
                            isMusicBarExpanded.update { true }
                        }

                        MusicBarEffect.CollapseMusicBar -> {
                            isMusicBarExpanded.update { false }
                        }
                    }
                }
            }
        }
    }

    override fun onTogglePlaybackClick() {
        playbackUserActions.togglePlaying()
    }

    override fun onSkipToNextClick() {
        playbackUserActions.skipToNext()
    }

    override fun onMusicBarClick() {

    }
}