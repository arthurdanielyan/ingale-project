package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api

import androidx.compose.runtime.Stable
import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface PlaybackScreenComponent {

    val uiState: StateFlow<PlaybackScreenViewState>
    val playbackProgress: StateFlow<Float>
    val uiCallbacks: PlaybackScreenUiCallbacks

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            onClose: () -> Unit,
        ): PlaybackScreenComponent
    }
}