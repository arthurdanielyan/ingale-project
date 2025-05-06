package com.nightx.ingale.featureLocal.featureHome.presentation.api

import com.nightx.ingale.core.decompose.AppComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalHomeComponent {

    val uiState: StateFlow<LocalMainScreenViewState>
    val uiEffect: Flow<LocalMainUiEffect>
    val uiCallbacks: LocalMainCallbacks

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext
        ): LocalHomeComponent
    }
}