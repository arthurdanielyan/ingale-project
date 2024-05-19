package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.destinations.DialogDestination
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for activating dialogs. Can be injected.
 * */
interface DialogNavigator {

    fun <A: Parcelable, R: Parcelable> activate(
        destination: DialogDestination,
        argument: A,
        onResult: ((R) -> Unit)
    )

    fun <A: Parcelable> activate(
        destination: DialogDestination,
        argument: A
    )

    fun <R: Parcelable> activate(
        destination: DialogDestination,
        onResult: ((R) -> Unit)
    )

    fun activate(destination: DialogDestination)

    fun dismiss()
}

interface ActiveDialogHolder {

    /**
     * Last active dialog must remain after configuration change, that's why this is a
     * [StateFlow].
     * */
    val activeDialog: StateFlow<DialogNavEvent<*, *>?>
}

val LocalDialogNavigation = compositionLocalOf<ActiveDialogHolder> {
    error("No DialogNavigator provided")
}