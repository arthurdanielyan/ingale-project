package com.nightx.ingale.core.domainModel

sealed interface LoadState<T> {
    data class Loading<T> (val lastSuccessfulData: T? = null) : LoadState<T>
    data class Success<T>(val data: T) : LoadState<T>

    data class Error<T> (
        val lastSuccessfulData: T?,
        val throwable: Throwable
    ) : LoadState<T>

    fun dataOrDefault(default: T): T =
        when(this) {
            is Success -> this.data
            is Error -> this.lastSuccessfulData ?: default
            else -> default
        }
}