package com.nightx.ingale.core.dataModel.room.playlists.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nightx.ingale.core.dataModel.room.playlists.PlaylistsTable
import com.nightx.ingale.core.dataModel.room.playlists.entity.PlaylistEntity
import com.nightx.ingale.core.dataModel.room.songsCache.SongsCacheTable
import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = PlaylistsTable.COLUMN_ID,
        entityColumn = SongsCacheTable.COLUMN_ID,
        associateBy = Junction(
            value = PlaylistSongCrossRef::class,
            parentColumn = PlaylistsTable.PlaylistSongCrossRef.COLUMN_PLAYLIST_ID,
            entityColumn = PlaylistsTable.PlaylistSongCrossRef.COLUMN_SONG_ID
        )
    )
    val songs: List<SongEntity>
)