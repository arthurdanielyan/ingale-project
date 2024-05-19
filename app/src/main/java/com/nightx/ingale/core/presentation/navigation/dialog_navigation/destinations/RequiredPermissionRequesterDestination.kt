package com.nightx.ingale.core.presentation.navigation.dialog_navigation.destinations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data object RequiredPermissionRequesterDestination : DialogDestination {

    override val route = "required_permissions_requester_dialog"

    @Parcelize
    class ResultData(
        val audioPermissionGranted: Boolean,
    ) : Parcelable
}