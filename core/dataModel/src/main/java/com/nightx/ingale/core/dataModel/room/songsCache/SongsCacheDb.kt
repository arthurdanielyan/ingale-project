package com.nightx.ingale.core.dataModel.room.songsCache

import com.nightx.ingale.core.dataModel.room.songsCache.dao.SongsCacheDao

interface SongsCacheDb {

    val songsCacheDao: SongsCacheDao
}