package com.nightx.ingale.root.api.root

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import kotlinx.coroutines.flow.StateFlow

@Stable
interface RootComponent {

    val uiState: StateFlow<BottomNavigationState>

    val childStack: Value<ChildStack<RootScreenConfig, Any>>
    val childSlot: Value<ChildSlot<PlaybackScreenConfig, PlaybackScreenComponent>>

    val snackbarComponent: SnackbarComponent
    val musicBarComponent: MusicBarComponent

    fun onTabSelected(tab: BottomBarItemViewState)

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext,
        ): RootComponent
    }
}