package com.nightx.ingale.featureLocal.featureHome.domain.model

import com.nightx.ingale.core.domainModel.Song

data class Album(
    val albumId: Long,
    val albumName: String,
    val songs: List<Song>
)