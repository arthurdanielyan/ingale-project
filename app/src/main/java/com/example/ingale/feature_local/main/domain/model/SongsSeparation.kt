package com.example.ingale.feature_local.main.domain.model

import com.example.ingale.core.domain.model.Song

data class SongsSeparation (
    val songs: List<Song>,
    val albums: List<Album>,
    val artists: List<Artist>
)