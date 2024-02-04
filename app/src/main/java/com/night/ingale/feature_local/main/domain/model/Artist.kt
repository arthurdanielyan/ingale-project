package com.night.ingale.feature_local.main.domain.model

import com.night.ingale.core.domain.model.Song
import com.night.ingale.mvi.wrappers.StableList

data class Artist(
    val artistId: Long,
    val artistName: String,
    val songs: StableList<Song>
)