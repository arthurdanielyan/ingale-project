package com.nightx.ingale.featureLocal.navigation.api.destinations

import android.os.Parcelable
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination
import kotlinx.parcelize.Parcelize

data object SongsSetScreenDestination : LocalScreenDestination {

    override val route = "local_songs_set_screen"

    @Parcelize
    data class SongsSet(
        val id: Long,
        val title: String,
        val songs: List<SongArg>,
        val iconPath: String = ""
    ): Parcelable
}

@Parcelize
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
): Parcelable