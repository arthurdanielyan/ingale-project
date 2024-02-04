package com.night.ingale.feature_local.main.domain.model

import com.night.ingale.core.domain.model.Song
import com.night.ingale.mvi.wrappers.StableList

data class Album(
    val albumId: Long,
    val albumName: String,
    val songs: StableList<Song>
)