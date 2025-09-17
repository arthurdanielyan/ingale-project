package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api

import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

interface CreatePlaylistDialogComponent : AppComponentContext {

    val uiState: StateFlow<CreatePlaylistDialogViewState>
    val uiCallbacks: CreatePlaylistDialogCallbacks

    data class Params(
        val initialSongId: Long? = null,
    )

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
            params: Params,
            onDismiss: () -> Unit,
        ): CreatePlaylistDialogComponent
    }
}