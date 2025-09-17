package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api

data class CreatePlaylistDialogViewState(
    val playlistName: String = "",
    val requestedPlaylistExistsError: Boolean = false,
    val isCreateButtonEnabled: Boolean = false,
)