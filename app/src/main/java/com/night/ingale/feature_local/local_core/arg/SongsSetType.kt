package com.night.ingale.feature_local.local_core.arg

sealed interface SongsSetType {
    object Playlist : SongsSetType { override val key = 0 }
    object Album : SongsSetType { override val key = 1 }
    object Artist : SongsSetType { override val key = 2 }

    val key: Int
}