package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import androidx.compose.runtime.compositionLocalOf
import com.nightx.ingale.feature_local.local_navigation.LocalNavEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LocalNavigatorImpl : LocalNavigator, LocalNavigatorEventsHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _navigationEvent = Channel<LocalNavEvent<*, *>>(
        capacity = UNLIMITED
    )
    override val navigationEvent = _navigationEvent.receiveAsFlow()

    override fun <A, R> navigate(
        destination: LocalScreenDestination,
        argument: A,
        onResult: (R) -> Unit,
    ) {
        navigateCommon<A, R>(destination, argument, onResult)
    }

    override fun <A> navigate(destination: LocalScreenDestination, argument: A) {
        navigateCommon<A, Unit?>(destination, argument)
    }

    override fun <R> navigate(destination: LocalScreenDestination, onResult: (R) -> Unit) {
        navigateCommon<Unit?, R>(destination, onResult = onResult)
    }

    override fun navigate(destination: LocalScreenDestination) {
        navigateCommon<Unit?, Unit?>(destination)
    }

    private fun <A, R> navigateCommon(
        destination: LocalScreenDestination,
        argument: A? = null,
        onResult: ((R) -> Unit)? = null,
    ) {
        scope.launch {
            _navigationEvent.send(
                LocalNavEvent(
                    destination = destination,
                    argument = argument,
                    onResult = onResult
                )
            )
        }
    }
}

val LocalNavigation = compositionLocalOf<LocalNavigatorEventsHolder> {
    throw Exception("No LocalNavigator provided")
}