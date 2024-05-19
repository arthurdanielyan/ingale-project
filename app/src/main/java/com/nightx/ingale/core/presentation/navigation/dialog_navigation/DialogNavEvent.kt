package com.nightx.ingale.core.presentation.navigation.dialog_navigation

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope

data class DialogNavEvent<A: Parcelable, R: Parcelable> (
    val destination: DialogDestination,
    val argument: A?,
    val onResult: ((R) -> Unit)?,
    val scope: CoroutineScope
)