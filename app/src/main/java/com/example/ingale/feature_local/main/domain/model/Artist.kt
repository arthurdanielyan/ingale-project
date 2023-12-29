package com.example.ingale.feature_local.main.domain.model

import com.example.ingale.core.domain.model.Song
import com.example.ingale.mvi.wrappers.StableList

data class Artist(
    val artistId: Long,
    val artistName: String,
    val songs: StableList<Song>
)