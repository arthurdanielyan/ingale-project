package com.nightx.ingale.globalPlaybackPresentation.musicBar.api

import androidx.compose.runtime.Stable
import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface MusicBarComponent {

    val uiState: StateFlow<MusicBarViewState>
    val uiCallbacks: MusicBarUiCallbacks

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            onMusicBarClicked: () -> Unit,
        ): MusicBarComponent
    }
}