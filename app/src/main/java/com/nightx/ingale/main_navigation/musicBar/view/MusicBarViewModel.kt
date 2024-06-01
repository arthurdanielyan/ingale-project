package com.nightx.ingale.main_navigation.musicBar.view

import androidx.lifecycle.viewModelScope
import com.nightx.ingale.core.audio_player.CurrentSongInfoStateHolder
import com.nightx.ingale.core.audio_player.actions.PlayerUiActions
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffect
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffectsHolder
import com.nightx.ingale.main_navigation.musicBar.CurrentSongInfo
import com.nightx.ingale.mvi.BaseViewModel
import com.nightx.ingale.mvi.UiEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicBarViewModel(
    currentSongInfoStateHolder: CurrentSongInfoStateHolder,
    private val playerUiActions: PlayerUiActions,
    private val bottomBarEffectsHolder: BottomBarEffectsHolder
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
            bottomBarEffectsHolder.bottomBarEffect.collectLatest {
                if(currentState.isMusicBarVisible) {
                    when (it) {
                        BottomBarEffect.ExpandMusicBar -> {
                            isMusicDetailsExpanded.update { true }
                        }

                        BottomBarEffect.CollapseMusicBar -> {
                            isMusicDetailsExpanded.update { false }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}