package com.night.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DialogNavigatorImpl : DialogNavigator, ActiveDialogHolder, ViewModelStoreOwner {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _activeDialog = Channel<DialogNavEvent<*,*>?>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val activeDialog = _activeDialog
        .receiveAsFlow()
        .stateIn(scope, SharingStarted.Eagerly, null)

    private val viewModelStores = mutableMapOf<DialogDestination, ViewModelStore>()

    override val viewModelStore: ViewModelStore
        get() {
            return viewModelStores[activeDialog.value?.destination]
                ?: throw IllegalAccessException("No active dialog")
        }


    override fun dismiss() {
        scope.launch {
            activeDialog.value?.destination?.let {
                viewModelStores.remove(it)?.clear()
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
        viewModelStores[destination] = ViewModelStore()
    }
}

val LocalDialogViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("No DialogViewModelStoreOwner provided")
}