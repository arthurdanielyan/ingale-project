package com.example.ingale.core.presentation.view

sealed class LoadingViewState {

    val isLoading: Boolean
        get() = this is Loading
    val isSuccess: Boolean
        get() = this is Loading
    val isError: Boolean
        get() = this is Loading

    data object Loading : LoadingViewState()
    data object Success : LoadingViewState()
    data class Error(val message: String): LoadingViewState()
}