package com.example.ingale.feature_local.local_navigation

import com.example.ingale.core.presentation.navigation.destination.NavEvent
import com.example.ingale.feature_local.local_navigation.screen_navigation.LocalScreenDestination

data class LocalNavEvent<A, R> (
    override val destination: LocalScreenDestination,
    override val argument: A?,
    override val onResult: ((R) -> Unit)?
): NavEvent<A, R>