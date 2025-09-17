package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api

import com.nightx.ingale.core.decompose.ScreenConfig
import kotlinx.serialization.Serializable

@Serializable
sealed interface SongOperationsChildConfig : ScreenConfig {

    @Serializable
    data class SongOperationsBottomSheetConfig(
        val songId: Long,
    ) : SongOperationsChildConfig

    @Serializable
    data class CreatePlaylistConfig(
        val initialSongId: Long
    ) : SongOperationsChildConfig

    @Serializable
    data class CurrentPlaylistsBottomSheet(
        val songId: Long
    ) : SongOperationsChildConfig

    @Serializable
    data class DeleteSongConfirmationConfig(
        val songId: Long
    ) : SongOperationsChildConfig
}