package com.nightx.ingale.feature_local.main.domain.model

import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.mvi.wrappers.StableList

data class Artist(
    val artistId: Long,
    val artistName: String,
    val songs: StableList<Song>
)