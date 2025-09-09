package com.nightx.ingale.core.domainModel.repository

interface SongsCacheRepository {

    suspend fun removeSong(id: Long)
}