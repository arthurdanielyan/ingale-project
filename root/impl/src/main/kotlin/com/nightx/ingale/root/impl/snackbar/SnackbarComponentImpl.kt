package com.nightx.ingale.root.impl.snackbar

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import com.nightx.ingale.root.api.snackbar.SnackbarMessageReceiver
import kotlinx.coroutines.flow.receiveAsFlow

internal class SnackbarComponentImpl(
    appComponentContext: AppComponentContext,
    snackbarMessageSender: SnackbarMessageSenderImpl,
) : SnackbarComponent,
    AppComponentContext by appComponentContext {

    override val snackbarMessageReceiver = object : SnackbarMessageReceiver {

        override val snackbarMessage =
            snackbarMessageSender.snackbarMessage.receiveAsFlow()
    }

}