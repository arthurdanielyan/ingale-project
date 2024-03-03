package com.nightx.ingale.feature_local.main.domain.repository

import com.nightx.ingale.core.domain.model.Song

interface LocalMainRepository {

    fun getSongs(): List<Song>
}