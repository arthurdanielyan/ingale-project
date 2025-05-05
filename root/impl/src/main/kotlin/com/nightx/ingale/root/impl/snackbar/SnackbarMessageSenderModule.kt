package com.nightx.ingale.root.impl.snackbar

import com.nightx.ingale.bottomBar.api.SnackbarMessageReceiver
import com.nightx.ingale.bottomBar.api.SnackbarMessageSender
import org.koin.dsl.module

val snackbarMessageSenderModule = module {
    val snackbarMessageSender = SnackbarMessageSenderImpl()
    single<SnackbarMessageSender> {
        snackbarMessageSender
    }
    single<SnackbarMessageReceiver> {
        snackbarMessageSender
    }
}