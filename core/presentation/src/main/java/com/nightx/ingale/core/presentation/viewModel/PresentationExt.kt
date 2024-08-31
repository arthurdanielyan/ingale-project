package com.nightx.ingale.core.presentation.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<T>.updateIf(condition: Boolean, modifier: (T) -> T) {
    if (condition) {
        update(modifier)
    }
}