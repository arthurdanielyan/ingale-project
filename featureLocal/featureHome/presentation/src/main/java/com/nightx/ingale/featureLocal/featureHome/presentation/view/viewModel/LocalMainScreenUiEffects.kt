package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel

import com.nightx.ingale.core.presentation.viewModel.UiEffect

internal sealed interface Effect : UiEffect {
    data object ScrollToTop : Effect
}