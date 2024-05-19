package com.nightx.ingale.core.presentation.navigation.coreNavigation

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Destination.Companion.RESULT_KEY

interface Destination {
    companion object {
        const val ARGUMENT_KEY = "navigation_default_argument_key"
        const val RESULT_KEY = "navigation_key_to_send_results"
    }

    val route: String
}

/**
 * Retrieves the argument passed from the previous screen.
 * */
fun <T> SavedStateHandle.getArgument() = this.get<T>(Destination.ARGUMENT_KEY)

/**
 * Sends result to the previous screen. Note that the receiver [SavedStateHandle] is the
 * [SavedStateHandle] of the CURRENT screen. The type of the result must be the
 * same as expected by the previous screen.
 * */
fun <T: Parcelable> SavedStateHandle.sendResult(result: T) {
    this[RESULT_KEY] = result
}

fun <A: Parcelable> SavedStateHandle.putScreenData(
    argument: A?,
) {
    this[Destination.ARGUMENT_KEY] = argument
}