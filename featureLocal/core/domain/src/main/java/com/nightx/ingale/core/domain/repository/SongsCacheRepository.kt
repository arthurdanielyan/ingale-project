package com.nightx.ingale.core.domain.repository

interface SongsCacheRepository {

    suspend fun removeSong(id: Long)
}