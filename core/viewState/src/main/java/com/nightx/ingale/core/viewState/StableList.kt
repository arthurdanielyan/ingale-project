package com.nightx.ingale.core.viewState

import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class StableList<out T> (
    private val value: List<T>
): List<T> by value

operator fun <T> StableList<T>.plus(element: T): StableList<T> =
    this.toMutableList().apply {
        add(element)
    }.toStableList()

operator fun <T> StableList<T>.minus(element: T): StableList<T> =
    this.toMutableList().apply {
        remove(element)
    }.toStableList()

fun <T> stableListOf(vararg elements: T): StableList<T> =
    StableList(listOf(*elements))

fun <T> emptyStableList(): StableList<T> = StableList(emptyList())

fun <T> List<T>.toStableList(): StableList<T> = StableList(this)

fun <T> Array<T>.toStableList(): StableList<T> = StableList(this.asList())
