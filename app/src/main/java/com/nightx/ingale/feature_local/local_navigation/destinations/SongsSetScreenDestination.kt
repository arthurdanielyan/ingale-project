package com.nightx.ingale.feature_local.local_navigation.destinations

import android.os.Parcelable
import com.nightx.ingale.core.domain.model.Song
import kotlinx.parcelize.Parcelize

data object SongsSetScreenDestination : LocalScreenDestination {

    override val route = "local_songs_set_screen"

    @Parcelize
    data class SongsSet(
        val id: Long,
        val title: String,
        val songs: List<Song>,
        val iconPath: String = ""
    ): Parcelable
}