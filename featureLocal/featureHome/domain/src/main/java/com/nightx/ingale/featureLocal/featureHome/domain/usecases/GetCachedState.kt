package com.nightx.ingale.featureLocal.featureHome.domain.usecases

import com.nightx.ingale.core.domain.songs.repository.SongsCacheRepository

class GetCachedState(
    private val repository: SongsCacheRepository,
) {

    suspend operator fun invoke(): Boolean =
        repository.isCached()
}