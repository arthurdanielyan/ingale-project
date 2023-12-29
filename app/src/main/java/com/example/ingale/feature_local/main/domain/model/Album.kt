package com.example.ingale.feature_local.main.domain.model

import com.example.ingale.core.domain.model.Song
import com.example.ingale.mvi.wrappers.StableList

data class Album(
    val albumId: Long,
    val albumName: String,
    val songs: StableList<Song>
)