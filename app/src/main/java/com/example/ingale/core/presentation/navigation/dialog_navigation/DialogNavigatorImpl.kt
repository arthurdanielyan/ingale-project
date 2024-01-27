package com.example.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.view.RequiredPermissionsRequesterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class DialogNavigatorImpl : DialogNavigator, ActiveDialogHolder, ViewModelStoreOwner {

    override var viewModelStore = ViewModelStore()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _activeDialog = Channel<DialogNavEvent<*,*>?>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val activeDialog: Flow<DialogNavEvent<*, *>?> = _activeDialog.consumeAsFlow()

    override fun dismiss() {
        scope.launch {
            _activeDialog.send(null)
            /**
             * In some cases the necessity of a dialog existence is determined in the init
             * block of its ViewModel when the ViewModel is not yet put in the ViewModelStore
             * and calling [ViewModelStore.clear] here doesn't remove the ViewModel and it
             * remains in the memory.
             * Particularly in the case of [RequiredPermissionsRequesterViewModel] when all
             * permissions are already granted.
             * */
            viewModelStore = ViewModelStore()
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
    }
}

val LocalDialogNavigation = compositionLocalOf<ActiveDialogHolder> {
    error("No DialogNavigator provided")
}

val LocalDialogViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("No DialogViewModelStoreOwner provided")
}