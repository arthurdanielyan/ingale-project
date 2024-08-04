package com.nightx.ingale.featureLocal.featureHome.domain.repository

import com.nightx.ingale.core.domainModel.LoadState
import com.nightx.ingale.core.domainModel.Song
import kotlinx.coroutines.flow.Flow

interface LocalMainRepository {

    suspend fun getSongs(): Flow<LoadState<List<Song>>>
}