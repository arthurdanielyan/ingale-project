package com.nightx.ingale.core.dataModel.repository

import com.nightx.ingale.core.dataModel.room.songsCache.dao.SongsCacheDao
import com.nightx.ingale.core.domain.repository.SongsCacheRepository

class SongsCacheRepositoryImpl(
    val songsCacheDao: SongsCacheDao,
) : SongsCacheRepository {

    override suspend fun removeSong(id: Long) {
        songsCacheDao.deleteById(id)
    }
}