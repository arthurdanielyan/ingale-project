package com.example.ingale.core.presentation.navigation.destination

import androidx.lifecycle.SavedStateHandle

interface Destination {
    companion object {
        const val ARGUMENT_KEY = "navigation_default_argument_key"
        const val ON_RESULT_KEY = "navigation_default_on_result_key"
    }

    val route: String
}

fun <T> SavedStateHandle.getArgument() = this.get<T>(Destination.ARGUMENT_KEY)
fun <T> SavedStateHandle.getOnResultCallback(): ((T) -> Unit)?
        = this[Destination.ON_RESULT_KEY]

fun <A, R> SavedStateHandle.putScreenData(argument: A?, onResult: ((R) -> Unit)?) {
    this[Destination.ARGUMENT_KEY] = argument
    this[Destination.ON_RESULT_KEY] = onResult
}