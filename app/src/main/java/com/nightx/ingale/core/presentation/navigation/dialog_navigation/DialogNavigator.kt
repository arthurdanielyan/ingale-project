package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Class for activating dialogs. Can be injected.
 * Each dialog has its own [ViewModel], which is destroyed at a proper moment, however
 * note that Dialog's lifecycle is not separate from its launcher's lifecycle. If you need to
 * have a separate lifecycle for dialog, add dialog to the NavHost at
 * [com/nightx/ingale/feature_local/local_navigation/screen_navigation/LocalSectionNavGraph.kt:26]
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