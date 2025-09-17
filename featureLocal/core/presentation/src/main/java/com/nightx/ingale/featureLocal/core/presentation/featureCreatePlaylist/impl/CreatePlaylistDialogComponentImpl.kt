package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.impl

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domain.playlists.usecase.CreateNewPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogCallbacks
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogViewState
import com.nightx.ingale.resources.strings.R
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CreatePlaylistDialogComponentImpl(
    appComponentContext: AppComponentContext,
    private val onDismiss: () -> Unit,
    private val params: CreatePlaylistDialogComponent.Params,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val createNewPlaylistUseCase: CreateNewPlaylistUseCase,
    private val applicationScope: CoroutineScope,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val stringProvider: StringProvider,
) : AppComponentContext by appComponentContext,
    CreatePlaylistDialogComponent,
    CreatePlaylistDialogCallbacks {

    private val existingPlaylists = MutableStateFlow(emptyList<String>())

    private val playlistName = MutableStateFlow("")
    private val doesRequestedPlaylistExist = combine(
        existingPlaylists,
        playlistName
    ) { existingPlaylists, playlistName ->
        playlistName in existingPlaylists
    }.stateIn(componentScope, SharingStarted.Eagerly, false)

    override val uiState = combine(
        playlistName,
        doesRequestedPlaylistExist
    ) { playlistName, doesRequestedPlaylistExist ->
        CreatePlaylistDialogViewState(
            playlistName = playlistName,
            requestedPlaylistExistsError = doesRequestedPlaylistExist,
            isCreateButtonEnabled = playlistName.isNotBlank() && doesRequestedPlaylistExist.not()
        )
    }.stateInWhileSubscribed(componentScope, CreatePlaylistDialogViewState())

    override val uiCallbacks = this

    init {
        getExistingPlaylist()
    }

    private fun getExistingPlaylist() {
        componentScope.launch {
            existingPlaylists.update {
                getPlaylistsUseCase().map { it.name }
            }
        }
    }

    override fun onPlaylistNameType(value: String) {
        playlistName.update { value }
    }

    override fun onCreateClick() {
        applicationScope.launch {
            createNewPlaylistUseCase(
                name = playlistName.value,
                initialSongId = params.initialSongId
            )
        }
        snackbarMessageSender.sendSnackbarMessage(
            stringProvider.string(R.string.playlist_created)
        )
        onDismiss()
    }

    override fun onCancelClick() {
        onDismiss()
    }
}