package com.nightx.ingale.feature_local.main.domain.repository

import com.nightx.ingale.core.domain.LoadState
import com.nightx.ingale.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface LocalMainRepository {

    suspend fun getSongs(): Flow<LoadState<List<Song>>>
}