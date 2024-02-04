package com.night.ingale.feature_local.local_navigation

import com.night.ingale.core.presentation.navigation.destination.NavEvent
import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalScreenDestination

data class LocalNavEvent<A, R> (
    override val destination: LocalScreenDestination,
    override val argument: A?,
    override val onResult: ((R) -> Unit)?
): NavEvent<A, R>