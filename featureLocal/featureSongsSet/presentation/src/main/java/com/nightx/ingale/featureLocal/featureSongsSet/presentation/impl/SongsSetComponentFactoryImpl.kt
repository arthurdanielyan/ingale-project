package com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl

import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongArgMapper
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.impl.mappers.SongsSetParamsMapper

internal class SongsSetComponentFactoryImpl(
    private val songArgMapper: SongArgMapper,
    private val songsSetParamsMapper: SongsSetParamsMapper,
    private val playerUiActions: PlayerUiActions,
) : SongsSetComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        params: SongsSetComponent.Params
    ): SongsSetComponent {
        return SongsSetComponentImpl(
            appComponentContext = appComponentContext,
            params = params,
            songArgMapper = songArgMapper,
            songsSetParamsMapper = songsSetParamsMapper,
            playerUiActions = playerUiActions,
        )
    }
}