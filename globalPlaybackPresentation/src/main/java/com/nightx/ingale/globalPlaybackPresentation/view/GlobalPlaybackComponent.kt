package com.nightx.ingale.globalPlaybackPresentation.view

import androidx.lifecycle.viewModelScope
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.presentation.viewModel.BaseViewModel
import com.nightx.ingale.core.presentation.viewModel.UiEffect
import com.nightx.ingale.globalPlaybackPresentation.musicBarController.MusicBarEffect
import com.nightx.ingale.globalPlaybackPresentation.musicBarController.MusicBarEffectsHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class GlobalPlaybackComponent(
    currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playerUiActions: PlayerUiActions,
    private val musicBarEffectsHolder: MusicBarEffectsHolder,
) : BaseViewModel<GlobalPlaybackViewState, UiEffect>(),
    GlobalPlaybackViewCallbacks {

    override fun defineInitialState() = GlobalPlaybackViewState()

    private val isMusicBarExpanded = MutableStateFlow(true)
    private val isPlaybackScreenVisible = MutableStateFlow(false)

    override val state = combine(
        currentSongInfoStateHolder.currentSongInfo,
        isMusicBarExpanded,
        isPlaybackScreenVisible,
    ) { currentSongInfo, isMusicBarExpanded, isPlaybackScreenVisible ->

        GlobalPlaybackViewState(
            currentSongInfo = currentSongInfo,
            isMusicBarExpanded = isMusicBarExpanded,
            isMusicBarVisible = currentSongInfo != CurrentSongInfo.Empty,
            isPlaybackScreenVisible = isPlaybackScreenVisible,
        )
    }.viewModelState()

    init {
        observeMusicBarEffects()
    }

    private fun observeMusicBarEffects() {
        viewModelScope.launch {
            musicBarEffectsHolder.musicBarEffect.collectLatest {
                if(currentState.isMusicBarVisible) when (it) {
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

    override fun togglePlaybackScreenVisibility(isVisible: Boolean) {
        isPlaybackScreenVisible.update { isVisible }
    }

    override fun onTogglePlaybackClick() {
        playerUiActions.togglePlaying()
    }

    override fun onSkipToNextClick() {
        playerUiActions.skipToNext()
    }

    override fun onSkipToPreviousClick() {
        playerUiActions.skipToPrevious()
    }

    override fun onMusicBarClick() {
        isPlaybackScreenVisible.update { true }
    }

    override fun onClosePlaybackScreenClick() {
        isPlaybackScreenVisible.update { false }
    }
}