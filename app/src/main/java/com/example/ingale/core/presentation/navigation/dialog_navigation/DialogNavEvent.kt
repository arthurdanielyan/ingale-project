package com.example.ingale.core.presentation.navigation.dialog_navigation

import com.example.ingale.core.presentation.navigation.destination.NavEvent

data class DialogNavEvent<A, R> (
    override val destination: DialogDestination,
    override val argument: A?,
    override val onResult: ((R) -> Unit)?
): NavEvent<A, R>