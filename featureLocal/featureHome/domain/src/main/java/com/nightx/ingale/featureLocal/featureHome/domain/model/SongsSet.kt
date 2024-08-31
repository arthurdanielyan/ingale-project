package com.nightx.ingale.featureLocal.featureHome.domain.model

import com.nightx.ingale.core.domainModel.Song

data class SongsSet(
    val id: Long,
    val type: SongsSetType,
    val title: String,
    val songs: List<Song>,
)

enum class SongsSetType {
    Album, Artist, Playlist
}