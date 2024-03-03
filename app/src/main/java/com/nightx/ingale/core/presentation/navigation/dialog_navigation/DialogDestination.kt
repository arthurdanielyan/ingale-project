package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import com.nightx.ingale.core.presentation.navigation.destination.Destination

sealed interface DialogDestination : Destination {

    data object RequiredPermissionRequester : DialogDestination {

        override val route = "required_permissions_requester_dialog"
    }
}