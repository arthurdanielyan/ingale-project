package com.nightx.ingale.root.impl.snackbar

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class SnackbarMessageSenderImpl(
    private val applicationScope: CoroutineScope,
) : SnackbarMessageSender {

    val snackbarMessage = Channel<String>(
        capacity = Channel.BUFFERED
    )

    override fun sendSnackbarMessage(message: String) {
        applicationScope.launch {
            snackbarMessage.send(message)
        }
    }
}