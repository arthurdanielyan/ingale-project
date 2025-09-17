package com.nightx.ingale.featureLocal.featureSongsSet.presentation.api

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.SongsSetUiCallbacks
import com.nightx.ingale.featureLocal.navigation.api.LocalScreenConfig
import kotlinx.coroutines.flow.StateFlow

interface SongsSetComponent {

    val uiState: StateFlow<SongsSetViewState>
    val uiCallbacks: SongsSetUiCallbacks

    data class Params(
        val id: Long,
        val title: String,
        val songs: List<LocalScreenConfig.SongsSet.SongArg>,
        val iconPath: String = "",
    )

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext,
            params: Params,
        ): SongsSetComponent
    }
}