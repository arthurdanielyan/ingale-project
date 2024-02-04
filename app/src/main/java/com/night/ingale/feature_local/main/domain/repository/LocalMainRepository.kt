package com.night.ingale.feature_local.main.domain.repository

import com.night.ingale.core.domain.model.Song

interface LocalMainRepository {

    fun getSongs(): List<Song>
}