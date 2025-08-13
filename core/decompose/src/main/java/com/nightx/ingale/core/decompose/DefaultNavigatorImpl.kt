package com.nightx.ingale.core.decompose

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class DefaultNavigatorImpl<in T : Destination> : Navigator<T> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _navigationEvent = Channel<NavEvent>(
        capacity = UNLIMITED
    )
    override val navigationEvent = _navigationEvent.receiveAsFlow()

    override fun <A : Parcelable, R : Parcelable> navigate(
        destination: T,
        argument: A,
        onResult: (R) -> Unit,
    ) {
        navigateCommon(destination, argument, onResult)
    }

    override fun <A : Parcelable> navigate(destination: T, argument: A) {
        navigateCommon<A, Nothing>(destination, argument)
    }

    override fun <R : Parcelable> navigate(
        destination: T,
        onResult: (R) -> Unit,
    ) {
        navigateCommon<Nothing, R>(destination, onResult = onResult)
    }

    override fun navigate(destination: T) {
        navigateCommon<Nothing, Nothing>(destination)
    }

    override fun navigateUp() {
        scope.launch {
            _navigationEvent.send(NavigateUpEvent)
        }
    }

    private fun <A: Parcelable, R: Parcelable> navigateCommon(
        destination: T,
        argument: A? = null,
        onResult: ((R) -> Unit)? = null,
    ) {
        scope.launch {
            _navigationEvent.send(
                NavigateEvent(
                    destination = destination,
                    argument = argument,
                    onResult = onResult
                )
            )
        }
    }
}