package com.example.ingale.mvi.wrappers

import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class StableList<out T> (
    private val value: List<T>
): List<T> by value

fun <T> stableListOf(vararg elements: T): StableList<T> =
    StableList(listOf(*elements))

fun <T> emptyStableList(): StableList<T> = StableList(emptyList())

fun <T> List<T>.toStableList(): StableList<T> = StableList(this)
