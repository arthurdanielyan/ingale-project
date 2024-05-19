package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import android.os.Parcelable
import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Navigator
import com.nightx.ingale.feature_local.local_navigation.LocalNavEvent
import com.nightx.ingale.feature_local.local_navigation.LocalNavigateEvent
import com.nightx.ingale.feature_local.local_navigation.LocalNavigateUpEvent
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LocalNavigator : Navigator<LocalScreenDestination, LocalNavEvent> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _navigationEvent = Channel<LocalNavEvent>(
        capacity = UNLIMITED
    )
    override val navigationEvent = _navigationEvent.receiveAsFlow()

    override fun <A: Parcelable, R: Parcelable> navigate(
        destination: LocalScreenDestination,
        argument: A,
        onResult: (R) -> Unit,
    ) {
        navigateCommon(destination, argument, onResult)
    }

    override fun <A: Parcelable> navigate(destination: LocalScreenDestination, argument: A) {
        navigateCommon<A, Nothing>(destination, argument)
    }

    override fun <R: Parcelable> navigate(
        destination: LocalScreenDestination,
        onResult: (R) -> Unit,
    ) {
        navigateCommon<Nothing, R>(destination, onResult = onResult)
    }

    override fun navigate(destination: LocalScreenDestination) {
        navigateCommon<Nothing, Nothing>(destination)
    }

    override fun navigateUp() {
        scope.launch {
            _navigationEvent.send(LocalNavigateUpEvent)
        }
    }

    private fun <A: Parcelable, R: Parcelable> navigateCommon(
        destination: LocalScreenDestination,
        argument: A? = null,
        onResult: ((R) -> Unit)? = null,
    ) {
        scope.launch {
            _navigationEvent.send(
                LocalNavigateEvent(
                    destination = destination,
                    argument = argument,
                    onResult = onResult
                )
            )
        }
    }
}

val LocalNavigation = staticCompositionLocalOf<LocalNavigator> {
    throw Exception("No LocalNavigator provided")
}