package com.nightx.ingale.bottomBar.api

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.Flow

interface SnackbarMessageSender {

    fun sendSnackbarMessage(message: String)
}

interface SnackbarMessageReceiver {

    val snackbarMessage: Flow<String>
}

val LocalSnackbarMessageReceiver = staticCompositionLocalOf<SnackbarMessageReceiver> {
    error("No SnackbarMessageReceiver provided")
}
