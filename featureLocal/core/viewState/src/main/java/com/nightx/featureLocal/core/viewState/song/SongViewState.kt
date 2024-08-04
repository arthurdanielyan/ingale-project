package com.nightx.featureLocal.core.viewState.song

import androidx.compose.runtime.Immutable

@Immutable
data class SongViewState(
    val id: Long,
    val title: String,
    val album: String,
    val duration: Long,
    val artist: String,
    val genre: String,
    val path: String,
    val picturePath: String,
    val artistId: Long,
    val albumId: Long,
    val lastModifier: Long,
)