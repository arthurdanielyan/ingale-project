package com.nightx.ingale.featureLocal.featureHome.presentation.impl

import android.content.Context
import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.presentation.osExt.api.PermissionInspector
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetCachedState
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetViewStateMapper
import com.nightx.ingale.featureLocal.featureRequirePermissions.api.RequirePermissionsComponent
import com.nightx.ingale.resources.strings.StringProvider

internal class LocalHomeComponentFactoryImpl(
    private val requirePermissionComponentFactory: RequirePermissionsComponent.Factory,
    private val permissionInspector: PermissionInspector,
    private val songsListComponentFactory: SongsListComponent.Factory,
    private val getSongsUseCase: GetSongsUseCase,
    private val getCachedState: GetCachedState,
    private val songViewStateMapper: SongViewStateMapper,
    private val songsSetViewStateMapper: SongsSetViewStateMapper,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
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
            requirePermissionComponentFactory = requirePermissionComponentFactory,
            songsListComponentFactory = songsListComponentFactory,
            permissionInspector = permissionInspector,
            getSongsUseCase = getSongsUseCase,
            getCachedState = getCachedState,
            songViewStateMapper = songViewStateMapper,
            songsSetViewStateMapper = songsSetViewStateMapper,
            organizeSongsUseCase = organizeSongsUseCase,
            songsSetToNavArgMapper = songsSetToNavArgMapper,
            applicationContext = applicationContext,
            playbackUserActions = playbackUserActions,
            snackbarMessageSender = snackbarMessageSender,
            stringProvider = stringProvider,
        )
    }
}