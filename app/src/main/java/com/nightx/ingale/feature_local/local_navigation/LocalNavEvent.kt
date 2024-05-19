package com.nightx.ingale.feature_local.local_navigation


import android.os.Parcelable
import com.nightx.ingale.core.presentation.navigation.coreNavigation.NavEvent
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination

sealed interface LocalNavEvent : NavEvent

data class LocalNavigateEvent<A: Parcelable, R: Parcelable>(
    val destination: LocalScreenDestination,
    val argument: A?,
    val onResult: ((R) -> Unit)?,
): LocalNavEvent

data object LocalNavigateUpEvent : LocalNavEvent