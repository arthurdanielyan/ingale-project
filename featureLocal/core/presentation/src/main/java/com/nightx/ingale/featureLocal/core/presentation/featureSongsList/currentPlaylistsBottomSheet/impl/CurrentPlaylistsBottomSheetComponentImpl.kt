package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.impl

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domain.playlists.usecase.AddSongToPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.core.viewState.toComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.PlaylistViewState
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.mapper.PlaylistViewStateMapper
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetUiCallbacks
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetViewState
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.nightx.ingale.featureLocal.core.presentation.R.string as LocalStrings

internal class CurrentPlaylistsBottomSheetComponentImpl(
    appComponentContext: AppComponentContext,
    private val params: CurrentPlaylistsBottomSheetComponent.Params,
    private val onCreateNewPlaylistCallback: () -> Unit,
    private val onDismissCallback: () -> Unit,
    private val applicationScope: CoroutineScope,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val playlistViewStateMapper: PlaylistViewStateMapper,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val stringProvider: StringProvider,
) : CurrentPlaylistsBottomSheetComponent,
    CurrentPlaylistsBottomSheetUiCallbacks,
    AppComponentContext by appComponentContext {

    private val playlists = MutableStateFlow<List<PlaylistViewState>>(emptyList())
    override val uiState = playlists.map { playlists ->
        CurrentPlaylistsBottomSheetViewState(
            playlists = playlists.toComposeList(),
        )
    }.stateInWhileSubscribed(componentScope, CurrentPlaylistsBottomSheetViewState())

    override val uiCallbacks = this

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        componentScope.launch {
            playlists.update {
                getPlaylistsUseCase().let(playlistViewStateMapper::mapList)
            }
        }
    }

    override fun onSaveToPlaylist(playlist: PlaylistViewState) {
        applicationScope.launch {
            addSongToPlaylistUseCase.invoke(
                playlistName = playlist.name,
                songId = params.songId,
            )
            snackbarMessageSender.sendSnackbarMessage(
                message = stringProvider
                    .string(
                        LocalStrings.added_to_playlist,
                        playlist.name
                    )
            )
        }
        onDismissCallback()
    }

    override fun onCreateNewPlaylistClick() {
        onCreateNewPlaylistCallback()
    }

    override fun onDismiss() {
        onDismissCallback()
    }
}