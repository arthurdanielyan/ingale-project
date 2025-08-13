package com.nightx.ingale.featureLocal.navigation.api

import com.nightx.ingale.core.decompose.ScreenConfig
import kotlinx.serialization.Serializable

@Serializable
sealed interface LocalScreenConfig : ScreenConfig {

    @Serializable
    data object Home : LocalScreenConfig

    @Serializable
    data class SongsSet(
        val id: Long,
        val title: String,
        val songs: List<SongArg>,
        val iconPath: String = "",
    ) : LocalScreenConfig {
        @Serializable
        data class SongArg(
            val id: Long,
            val title: String,
            val album: String,
            val duration: Long,
            val artist: String,
            val genre: String,
            val path: String,
            val picturePath: String,
            val artistId: Long,
            val albumId: Long,
            val lastModified: Long,
        )
    }
}