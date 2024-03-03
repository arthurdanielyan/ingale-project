package com.nightx.ingale.feature_local.main.domain.model

import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.mvi.wrappers.StableList

data class Album(
    val albumId: Long,
    val albumName: String,
    val songs: StableList<Song>
)