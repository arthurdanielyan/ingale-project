package com.nightx.ingale.musicbar.view

import androidx.lifecycle.viewModelScope
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.presentation.viewModel.BaseViewModel
import com.nightx.ingale.core.presentation.viewModel.UiEffect
import com.nightx.ingale.musicbar.musicBarControls.MusicBarEffect
import com.nightx.ingale.musicbar.musicBarControls.MusicBarEffectsHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MusicBarViewModel(
    currentSongInfoStateHolder: CurrentSongInfoStateProvider,
    private val playerUiActions: PlayerUiActions,
    private val musicBarEffectsHolder: MusicBarEffectsHolder
) : BaseViewModel<MusicBarViewState, UiEffect>(), PlayerUiActions by playerUiActions {

    override fun defineInitialState() = MusicBarViewState()

    private val isMusicDetailsExpanded = MutableStateFlow(true)

    override val state = combine(
        currentSongInfoStateHolder.currentSongInfo,
        isMusicDetailsExpanded
    ) { currentSongInfo, isMusicDetailsExpanded ->

        MusicBarViewState(
            currentSongInfo = currentSongInfo,
            isMusicDetailsExpanded = isMusicDetailsExpanded,
            isMusicBarVisible = currentSongInfo != CurrentSongInfo.Empty
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
                        isMusicDetailsExpanded.update { true }
                    }

                    MusicBarEffect.CollapseMusicBar -> {
                        isMusicDetailsExpanded.update { false }
                    }
                }
            }
        }
    }
}