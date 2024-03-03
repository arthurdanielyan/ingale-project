package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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

    /**
     * Last active dialog must remain after configuration change, that's why this is a
     * [StateFlow]. If this were a [Flow] the Dialog would disappear and the
     * [ViewModel] of it would remain in the memory unless a new dialog
     * were activated and dismissed
     * */
    val activeDialog: StateFlow<DialogNavEvent<*, *>?>
}

val LocalDialogNavigation = compositionLocalOf<ActiveDialogHolder> {
    error("No DialogNavigator provided")
}