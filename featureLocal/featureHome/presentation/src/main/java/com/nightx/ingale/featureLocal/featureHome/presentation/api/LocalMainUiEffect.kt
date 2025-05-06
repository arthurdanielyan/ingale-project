package com.nightx.ingale.featureLocal.featureHome.presentation.api

sealed interface LocalMainUiEffect {
    data object ScrollToTop : LocalMainUiEffect
}