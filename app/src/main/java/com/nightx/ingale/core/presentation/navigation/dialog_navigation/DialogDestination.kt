package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Destination
import kotlinx.parcelize.Parcelize

sealed interface DialogDestination : Destination {

    data object RequiredPermissionRequesterDestination : DialogDestination {

        override val route = "required_permissions_requester_dialog"

        @Parcelize
        class ResultData(
            val audioPermissionGranted: Boolean,
        ) : Parcelable
    }
}