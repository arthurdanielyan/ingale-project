package com.nightx.ingale.core.dataModel.room.playlists.entity.relations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.nightx.ingale.core.dataModel.room.playlists.PlaylistsTable
import com.nightx.ingale.core.dataModel.room.playlists.entity.PlaylistEntity
import com.nightx.ingale.core.dataModel.room.songsCache.SongsCacheTable
import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity

@Entity(
    primaryKeys = [
        PlaylistsTable.PlaylistSongCrossRef.COLUMN_PLAYLIST_ID,
        PlaylistsTable.PlaylistSongCrossRef.COLUMN_SONG_ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = [PlaylistsTable.COLUMN_ID],
            childColumns = [PlaylistsTable.PlaylistSongCrossRef.COLUMN_PLAYLIST_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = [SongsCacheTable.COLUMN_ID],
            childColumns = [PlaylistsTable.PlaylistSongCrossRef.COLUMN_SONG_ID],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlaylistSongCrossRef(
    @ColumnInfo(name = PlaylistsTable.PlaylistSongCrossRef.COLUMN_PLAYLIST_ID)
    val playlistId: Int,

    @ColumnInfo(name = PlaylistsTable.PlaylistSongCrossRef.COLUMN_SONG_ID)
    val songId: Long,
)