package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import com.nightx.ingale.feature_local.local_navigation.LocalNavEvent
import kotlinx.coroutines.flow.Flow

interface LocalNavigator {

    fun <A, R> navigate(
        destination: LocalScreenDestination,
        argument: A,
        onResult: ((R) -> Unit)
    )

    fun <A> navigate(
        destination: LocalScreenDestination,
        argument: A
    )

    fun <R> navigate(
        destination: LocalScreenDestination,
        onResult: ((R) -> Unit)
    )

    fun navigate(destination: LocalScreenDestination)
}

interface LocalNavigatorEventsHolder {
    val navigationEvent: Flow<LocalNavEvent<*, *>>
        get() = throw IllegalAccessException("Cannot access navigationEvents from here")
}