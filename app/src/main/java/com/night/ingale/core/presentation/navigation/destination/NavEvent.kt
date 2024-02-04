package com.night.ingale.core.presentation.navigation.destination

interface NavEvent<A, R> {
    val destination: Destination
    val argument: A?
    val onResult: ((R) -> Unit)?
}