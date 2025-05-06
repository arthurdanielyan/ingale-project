package com.nightx.ingale.root.api.snackbar

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.decompose.AppComponentContext

@Immutable
interface SnackbarComponent {

    val snackbarMessageReceiver: SnackbarMessageReceiver

    fun interface Factory {
        operator fun invoke(
            appComponentContext: AppComponentContext,
        ): SnackbarComponent
    }
}