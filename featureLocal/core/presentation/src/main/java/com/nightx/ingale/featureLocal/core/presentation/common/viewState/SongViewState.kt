package com.nightx.ingale.featureLocal.core.presentation.common.viewState

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