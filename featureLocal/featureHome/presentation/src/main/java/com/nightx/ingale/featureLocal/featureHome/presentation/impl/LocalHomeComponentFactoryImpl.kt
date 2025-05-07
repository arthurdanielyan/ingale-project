package com.nightx.ingale.featureLocal.featureHome.presentation.impl

import android.content.Context
import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.featureLocal.core.ui.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetCachedState
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetViewStateMapper
import com.nightx.ingale.resources.strings.StringProvider

internal class LocalHomeComponentFactoryImpl(
    private val getSongsUseCase: GetSongsUseCase,
    private val getCachedState: GetCachedState,
    private val songViewStateMapper: SongViewStateMapper,
    private val songsSetViewStateMapper: SongsSetViewStateMapper,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val requiredPermissionsInspector: RequiredPermissionsInspector,
    private val songsSetToNavArgMapper: SongsSetToNavArgMapper,
    private val applicationContext: Context,
    private val playbackUserActions: PlaybackUserActions,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val stringProvider: StringProvider,
) : LocalHomeComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ): LocalHomeComponent {
        return LocalHomeComponentImpl(
            appComponentContext = appComponentContext,
            getSongsUseCase = getSongsUseCase,
            getCachedState = getCachedState,
            songViewStateMapper = songViewStateMapper,
            songsSetViewStateMapper = songsSetViewStateMapper,
            organizeSongsUseCase = organizeSongsUseCase,
            requiredPermissionsInspector = requiredPermissionsInspector,
            songsSetToNavArgMapper = songsSetToNavArgMapper,
            applicationContext = applicationContext,
            playbackUserActions = playbackUserActions,
            snackbarMessageSender = snackbarMessageSender,
            stringProvider = stringProvider,
        )
    }
}