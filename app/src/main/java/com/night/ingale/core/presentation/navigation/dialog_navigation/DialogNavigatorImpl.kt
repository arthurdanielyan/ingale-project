package com.night.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DialogNavigatorImpl : DialogNavigator, ActiveDialogHolder, DialogViewModelStore {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _activeDialog = Channel<DialogNavEvent<*,*>?>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val activeDialog = _activeDialog
        .receiveAsFlow()
        .stateIn(scope, SharingStarted.Eagerly, null)

    private val viewModelStoreOwners = mutableMapOf<DialogDestination, ViewModelStoreOwner>()

    override val currentViewModelStore: ViewModelStoreOwner
        get() {
            val lastDestination = activeDialog.value?.destination
                ?: throw IllegalAccessException("No active dialog")
            return viewModelStoreOwners[lastDestination]
                ?: throw IllegalAccessException("No active dialog")
        }


    override fun dismiss() {
        scope.launch {
            activeDialog.value?.destination?.let {
                viewModelStoreOwners.remove(it)
            }
            _activeDialog.send(null)
        }
    }

    override fun <A, R> activate(
        destination: DialogDestination,
        argument: A,
        onResult: (R) -> Unit,
    ) {
        activateCommon(destination, argument, onResult)
    }

    override fun <A> activate(destination: DialogDestination, argument: A) {
        activateCommon<A, Unit?>(destination, argument)
    }

    override fun <R> activate(destination: DialogDestination, onResult: (R) -> Unit) {
        activateCommon<Unit?, R>(destination, onResult = onResult)
    }

    override fun activate(destination: DialogDestination) {
        activateCommon<Unit?, Unit?>(destination)
    }

    private fun <A, R> activateCommon(
        destination: DialogDestination,
        argument: A? = null,
        onResult: ((R) -> Unit)? = null,
    ) {
        scope.launch {
            _activeDialog.send(
                DialogNavEvent(
                    destination = destination,
                    argument = argument,
                    onResult = onResult
                )
            )
        }
        viewModelStoreOwners[destination] = object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    }
}

val LocalDialogViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("No DialogViewModelStoreOwner provided")
}