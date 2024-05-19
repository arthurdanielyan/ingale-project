package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.destinations.DialogDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Each dialog has its own [ViewModel], which is destroyed at a proper moment, however
 * note that Dialog's lifecycle is not separate from its launcher's lifecycle. If you need to
 * have a separate lifecycle for dialog, add dialog to the NavHost at
 * [com/nightx/ingale/feature_local/local_navigation/screen_navigation/LocalSectionNavGraph.kt:34]
 * */
class DialogNavigatorImpl : DialogNavigator, ActiveDialogHolder, ViewModelStoreOwner {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _activeDialog = Channel<DialogNavEvent<*, *>?>(
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

    override fun <A: Parcelable, R: Parcelable> activate(
        destination: DialogDestination,
        argument: A,
        onResult: (R) -> Unit
    ) {
        activateCommon(destination, argument, onResult)
    }

    override fun <A: Parcelable> activate(
        destination: DialogDestination,
        argument: A,
    ) {
        scope.launch {
            _activeDialog.send(
                DialogNavEvent<A, Parcelable>(
                    destination = destination,
                    argument = argument,
                    onResult = null,
                )
            )
        }
    }

    override fun <R: Parcelable> activate(
        destination: DialogDestination,
        onResult: (R) -> Unit,
    ) {
        scope.launch {
            _activeDialog.send(
                DialogNavEvent<Parcelable, R>(
                    destination = destination,
                    argument = null,
                    onResult = onResult,
                )
            )
        }
    }

    override fun activate(destination: DialogDestination) {
        scope.launch {
            _activeDialog.send(
                DialogNavEvent<Parcelable, Parcelable>(
                    destination = destination,
                    argument = null,
                    onResult = null,
                )
            )
        }
    }

    private fun <A: Parcelable, R: Parcelable> activateCommon(
        destination: DialogDestination,
        argument: A? = null,
        onResult: ((R) -> Unit)? = null,
    ) {
        scope.launch {
            _activeDialog.send(
                DialogNavEvent(
                    destination = destination,
                    argument = argument,
                    onResult = onResult,
                )
            )
        }
        viewModelStores[destination] = ViewModelStore()
    }
}

val LocalDialogViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    error("No DialogViewModelStoreOwner provided")
}