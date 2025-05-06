package com.nightx.ingale.core.viewState

import com.nightx.ingale.core.domainModel.LoadState

sealed interface LoadingViewState {
    data object Loading : LoadingViewState
    data object Success : LoadingViewState
    data class Error(val message: String) : LoadingViewState
}

fun LoadingViewState.isLoading(): Boolean =
    this is LoadingViewState.Loading

fun LoadingViewState.isSuccess(): Boolean =
    this is LoadingViewState.Success

fun LoadingViewState.isError(): Boolean =
    this is LoadingViewState.Error

fun LoadState<*>.toLoadingViewState(): LoadingViewState =
    when(this) {
        is LoadState.Loading -> LoadingViewState.Loading
        is LoadState.Success -> LoadingViewState.Success
        is LoadState.Error -> LoadingViewState.Error(
            this.throwable.message ?: ""
        )
    }