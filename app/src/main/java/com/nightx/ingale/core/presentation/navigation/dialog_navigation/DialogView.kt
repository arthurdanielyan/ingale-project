package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Destination
import com.nightx.ingale.core.presentation.navigation.coreNavigation.putScreenData
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogDestination.RequiredPermissionRequesterDestination
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.RequiredPermissionsRequesterDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun DialogView() {
    val activeDialog = LocalDialogNavigation.current
    DialogSlot(dialogNavEvent = activeDialog.activeDialog)
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun DialogSlot(dialogNavEvent: Flow<DialogNavEvent<*, *>?>) {
    val navEvent by dialogNavEvent.collectAsStateWithLifecycle(null)

    when (navEvent?.destination) {
        RequiredPermissionRequesterDestination -> {
            val event = navEvent as DialogNavEvent<Parcelable, Parcelable>
            RequiredPermissionsRequesterDialog(
                savedStateHandle = createDialogSavedStateHandle(event)
            )
        }

        null -> Unit
    }
}

private fun createDialogSavedStateHandle(
    navEvent: DialogNavEvent<Parcelable, Parcelable>
): SavedStateHandle {
    return SavedStateHandle().apply {
        putScreenData(
            argument = navEvent.argument
        )

        navEvent.onResult?.let { onResult: (RequiredPermissionRequesterDestination.ResultData) -> Unit ->
            getStateFlow<RequiredPermissionRequesterDestination.ResultData?>(Destination.RESULT_KEY, null)
                .onEach {
                    it?.let {
                        onResult(it)
                    }
                }.launchIn(navEvent.scope)
        }
    }
}
