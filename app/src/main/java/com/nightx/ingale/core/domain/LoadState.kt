package com.nightx.ingale.core.domain

import com.nightx.ingale.core.presentation.view.LoadingViewState

sealed interface LoadState<T> {
    data class Loading<T> (val lastSuccessfulData: T? = null) : LoadState<T>
    data class Success<T>(val data: T) : LoadState<T>

    data class Error<T> (
        val lastSuccessfulData: T?,
        val throwable: Throwable
    ) : LoadState<T>


    fun toLoadingViewState(): LoadingViewState =
        when(this) {
            is Loading -> LoadingViewState.Loading
            is Success -> LoadingViewState.Success
            is Error -> LoadingViewState.Error(this.throwable.message ?: "")
        }

    fun getOrDefault(default: T): T =
        when(this) {
            is Success -> this.data
            else -> default
        }
}