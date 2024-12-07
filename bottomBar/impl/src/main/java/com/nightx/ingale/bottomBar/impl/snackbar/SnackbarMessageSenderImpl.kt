package com.nightx.ingale.bottomBar.impl.snackbar

import com.nightx.ingale.bottomBar.api.SnackbarMessageReceiver
import com.nightx.ingale.bottomBar.api.SnackbarMessageSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class SnackbarMessageSenderImpl : SnackbarMessageSender, SnackbarMessageReceiver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _snackbarMessage = Channel<String>()
    override val snackbarMessage = _snackbarMessage.receiveAsFlow()

    override fun sendSnackbarMessage(message: String) {
        scope.launch {
            _snackbarMessage.send(message)
        }
    }
}