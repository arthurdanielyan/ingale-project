package com.example.ingale.core.presentation.navigation.dialog_navigation

import com.example.ingale.core.presentation.navigation.destination.Destination

sealed interface DialogDestination : Destination {
    sealed interface LocalDialogDestination : DialogDestination {

        data object RequiredPermissionRequester : LocalDialogDestination {

            override val route = "required_permissions_requester_dialog"
        }
    }
}