package com.nightx.ingale.featureLocal.featureHome.domain.model

import com.nightx.ingale.core.domainModel.Song

data class SongsSeparation (
    val songs: List<Song>,
    val albums: List<Album>,
    val artists: List<Artist>
)