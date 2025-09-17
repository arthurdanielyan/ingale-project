package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.impl

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domain.playlists.usecase.AddSongToPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.PlaylistViewStateMapper
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope

internal class CurrentPlaylistsBottomSheetComponentFactoryImpl(
    private val applicationScope: CoroutineScope,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val playlistViewStateMapper: PlaylistViewStateMapper,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val stringProvider: StringProvider,
) :
    CurrentPlaylistsBottomSheetComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext,
        params: CurrentPlaylistsBottomSheetComponent.Params,
        onCreateNewPlaylistClick: () -> Unit,
        onDismiss: () -> Unit
    ): CurrentPlaylistsBottomSheetComponent {
        return CurrentPlaylistsBottomSheetComponentImpl(
            appComponentContext = appComponentContext,
            params = params,
            onCreateNewPlaylistCallback = onCreateNewPlaylistClick,
            onDismissCallback = onDismiss,
            applicationScope = applicationScope,
            getPlaylistsUseCase = getPlaylistsUseCase,
            playlistViewStateMapper = playlistViewStateMapper,
            addSongToPlaylistUseCase = addSongToPlaylistUseCase,
            snackbarMessageSender = snackbarMessageSender,
            stringProvider = stringProvider,
        )
    }
}