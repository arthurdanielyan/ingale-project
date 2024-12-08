package com.nightx.ingale.featureLocal.featureHome.domain.usecases

import com.nightx.ingale.featureLocal.featureHome.domain.repository.LocalMainRepository

class GetCachedState(
    private val repository: LocalMainRepository,
) {

    suspend operator fun invoke(): Boolean =
        repository.isCached()
}