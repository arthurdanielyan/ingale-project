package com.example.ingale.feature_local.main.domain.repository

import com.example.ingale.core.domain.model.Song

interface LocalMainRepository {

    fun getSongs(): List<Song>
}