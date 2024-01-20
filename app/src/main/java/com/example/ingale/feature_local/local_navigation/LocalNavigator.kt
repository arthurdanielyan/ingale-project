package com.example.ingale.feature_local.local_navigation

import kotlinx.coroutines.flow.Flow

interface LocalNavigator {
    fun navigate(
        destination: LocalDestination,
        argument: Any?
    )
}

interface LocalNavigatorEventsHolder {
    val navigationEvent: Flow<LocalNavEvent<*>>
        get() = throw IllegalAccessException("Cannot access navigationEvents from here")
}