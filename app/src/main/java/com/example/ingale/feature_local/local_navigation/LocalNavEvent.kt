package com.example.ingale.feature_local.local_navigation

data class LocalNavEvent<T>(
    val destination: LocalDestination,
    val argument: T?
)