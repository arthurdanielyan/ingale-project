package com.example.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.Flow

interface DialogNavigator {

    fun <A, R> activate(
        destination: DialogDestination,
        argument: A,
        onResult: ((R) -> Unit)
    )

    fun <A> activate(
        destination: DialogDestination,
        argument: A
    )

    fun <R> activate(
        destination: DialogDestination,
        onResult: ((R) -> Unit)
    )

    fun activate(destination: DialogDestination)

    fun dismiss()
}

interface ActiveDialogHolder {

    val activeDialog: Flow<DialogNavEvent<*, *>?>
}

val LocalDialogNavigation = compositionLocalOf<ActiveDialogHolder> {
    error("No DialogNavigator provided")
}