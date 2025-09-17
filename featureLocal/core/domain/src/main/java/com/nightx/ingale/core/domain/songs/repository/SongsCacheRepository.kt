package com.nightx.ingale.core.domain.songs.repository

import com.nightx.ingale.core.domain.songs.model.Song
import com.nightx.ingale.core.utils.LoadState
import kotlinx.coroutines.flow.Flow

interface SongsCacheRepository {

    suspend fun removeSong(id: Long)

    suspend fun getSongs(): Flow<LoadState<List<Song>>>

    suspend fun isCached(): Boolean
}