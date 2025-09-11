package com.nightx.ingale.core.dataModel.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nightx.ingale.core.dataModel.room.playlists.PlaylistsDao
import com.nightx.ingale.core.dataModel.room.playlists.entity.PlaylistEntity
import com.nightx.ingale.core.dataModel.room.playlists.entity.relations.PlaylistSongCrossRef
import com.nightx.ingale.core.dataModel.room.songsCache.dao.SongsCacheDao
import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract val songsCacheDao: SongsCacheDao
    abstract val playlistsDao: PlaylistsDao
}