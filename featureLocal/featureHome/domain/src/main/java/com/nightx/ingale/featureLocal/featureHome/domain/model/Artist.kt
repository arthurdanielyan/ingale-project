package com.nightx.ingale.featureLocal.featureHome.domain.model

import com.nightx.ingale.core.domainModel.Song

data class Artist(
    val artistId: Long,
    val artistName: String,
    val songs: List<Song>
)