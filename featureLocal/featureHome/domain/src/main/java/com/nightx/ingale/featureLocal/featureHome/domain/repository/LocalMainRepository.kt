package com.nightx.ingale.featureLocal.featureHome.domain.repository

import com.nightx.ingale.core.domainModel.model.Song
import com.nightx.ingale.core.utils.LoadState
import kotlinx.coroutines.flow.Flow

interface LocalMainRepository {

    suspend fun getSongs(): Flow<LoadState<List<Song>>>

    suspend fun isCached(): Boolean
}