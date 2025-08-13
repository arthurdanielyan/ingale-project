package com.nightx.ingale.root.impl.snackbar

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.root.api.snackbar.SnackbarComponent

internal class SnackbarComponentFactoryImpl(
    private val snackbarMessageSender: SnackbarMessageSenderImpl,
) : SnackbarComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ): SnackbarComponent {
        return SnackbarComponentImpl(
            appComponentContext = appComponentContext,
            snackbarMessageSender = snackbarMessageSender,
        )
    }
}