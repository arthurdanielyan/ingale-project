package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api

import androidx.compose.runtime.Immutable

@Immutable
interface CreatePlaylistDialogCallbacks {

    fun onPlaylistNameType(value: String)
    fun onCreateClick()
    fun onCancelClick()
}