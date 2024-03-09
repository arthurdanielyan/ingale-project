package com.nightx.ingale.feature_local.songs_set.presentation.view

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song

@Immutable
interface SongsSetCallbacks {

    fun onSongClick(song: Song)
}