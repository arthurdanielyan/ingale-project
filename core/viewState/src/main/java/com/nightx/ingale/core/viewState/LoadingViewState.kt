package com.nightx.ingale.core.viewState

import com.nightx.ingale.core.domainModel.LoadState

sealed class LoadingViewState {

    val isLoading: Boolean
        get() = this is Loading
    val isSuccess: Boolean
        get() = this is Loading
    val isError: Boolean
        get() = this is Error

    data object Loading : LoadingViewState()
    data object Success : LoadingViewState()
    data class Error(val message: String): LoadingViewState()
}

fun LoadState<*>.toLoadingViewState(): LoadingViewState =
    when(this) {
        is LoadState.Loading -> LoadingViewState.Loading
        is LoadState.Success -> LoadingViewState.Success
        is LoadState.Error -> LoadingViewState.Error(
            this.throwable.message ?: ""
        )
    }