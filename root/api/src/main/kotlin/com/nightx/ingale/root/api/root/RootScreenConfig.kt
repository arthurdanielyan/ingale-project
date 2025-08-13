package com.nightx.ingale.root.api.root

import com.nightx.ingale.core.decompose.ScreenConfig
import kotlinx.serialization.Serializable

@Serializable
sealed interface RootScreenConfig : ScreenConfig {

    @Serializable
    data object LocalConfig : RootScreenConfig

    @Serializable
    data object YoutubeConfig : RootScreenConfig
}

@Serializable
data object PlaybackScreenConfig : ScreenConfig

fun RootScreenConfig.toBottomBarItemViewState(): BottomBarItemViewState {
    return when (this) {
        RootScreenConfig.LocalConfig -> BottomBarItemViewState.Local
        RootScreenConfig.YoutubeConfig -> BottomBarItemViewState.Youtube
    }
}
