package com.example.ingale.feature_local.local_navigation

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LocalNavigatorImpl : LocalNavigator, LocalNavigatorEventsHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _navigationEvent = Channel<LocalNavEvent<*>>(
        capacity = UNLIMITED
    )
    override val navigationEvent = _navigationEvent.receiveAsFlow()
    override fun navigate(destination: LocalDestination, argument: Any?) {
        scope.launch {
            _navigationEvent.send(
                LocalNavEvent(
                    destination = destination,
                    argument = argument
                )
            )
        }
    }
}

val LocalNavigation = compositionLocalOf<LocalNavigatorEventsHolder> {
    throw Exception("No LocalNavigator provided")
}