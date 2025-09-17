package com.nightx.ingale.core.dataModel.room.playlists

object PlaylistsTable {
    const val TABLE_NAME = "playlists"

    const val COLUMN_NAME = "name"

    object PlaylistSongCrossRef {
        const val COLUMN_PLAYLIST_NAME = "playlist_NAME"
        const val COLUMN_SONG_ID = "song_id"
    }
}