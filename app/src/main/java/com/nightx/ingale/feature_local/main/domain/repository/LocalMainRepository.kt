package com.nightx.ingale.feature_local.main.domain.repository

import com.nightx.ingale.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface LocalMainRepository {

    fun getSongs(): Flow<List<Song>>
}