package com.night.ingale.core.presentation.ui

import androidx.compose.runtime.Composable
import com.night.ingale.core.presentation.view.LoadingViewState

@Composable
fun LoadingStatePresenter(
    loadingState: LoadingViewState,
    loadingView: @Composable () -> Unit,
    errorView: @Composable (LoadingViewState.Error) -> Unit,
    successView: @Composable () -> Unit,
) {
    when(loadingState) {
        LoadingViewState.Loading -> loadingView()
        is LoadingViewState.Error -> errorView(loadingState)
        LoadingViewState.Success -> successView()
    }
}

@Composable
fun LoadingStatePresenter(
    loadingState: LoadingViewState,
    errorView: @Composable (LoadingViewState.Error) -> Unit,
    notErrorView: @Composable () -> Unit
) {
    when(loadingState) {
        is LoadingViewState.Error -> errorView(loadingState)
        else -> notErrorView()
    }
}