package com.example.ingale.core.presentation.navigation.dialog_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ingale.core.presentation.navigation.destination.putScreenData
import com.example.ingale.core.presentation.navigation.dialog_navigation.DialogDestination.LocalDialogDestination
import com.example.ingale.feature_local.required_permissions_requester_dialog.presentation.RequiredPermissionsRequesterDialog
import kotlinx.coroutines.flow.Flow

@Composable
fun DialogView() {
    val activeDialog = LocalDialogNavigation.current
    DialogSlot(dialogNavEvent = activeDialog.activeDialog)
}

@Composable
private fun DialogSlot(dialogNavEvent: Flow<DialogNavEvent<*,*>?>) {
    val currentDialog by dialogNavEvent.collectAsStateWithLifecycle(null)
    when(val dialogDestination = currentDialog?.destination) {
        is LocalDialogDestination -> {
            when(dialogDestination) {
                LocalDialogDestination.RequiredPermissionRequester -> {
                    RequiredPermissionsRequesterDialog(
                        savedStateHandle = SavedStateHandle().apply {
                            putScreenData(currentDialog?.argument, currentDialog?.onResult)
                        }
                    )
                }
            }
        }
        null -> Unit
    }
}

/*
* composable(
            route = LocalDestination.RequiredPermissionsScreen.route
        ) {

        }
* */