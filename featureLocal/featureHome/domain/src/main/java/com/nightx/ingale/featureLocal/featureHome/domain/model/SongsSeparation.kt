package com.nightx.ingale.featureLocal.featureHome.domain.model

import com.nightx.ingale.core.domain.model.Song

data class SongsSeparation(
    val songs: List<Song>,
    val albums: List<SongsSet>,
    val artists: List<SongsSet>,
)