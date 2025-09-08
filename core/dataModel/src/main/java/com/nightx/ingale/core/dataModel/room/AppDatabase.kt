package com.nightx.ingale.core.dataModel.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nightx.ingale.core.dataModel.room.songsCache.SongsCacheDb
import com.nightx.ingale.core.dataModel.room.songsCache.dao.SongsCacheDao
import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity

@Database(
    entities = [
        SongEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase(),
    SongsCacheDb {

    abstract override val songsCacheDao: SongsCacheDao
}