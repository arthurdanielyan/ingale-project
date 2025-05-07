package com.nightx.ingale.core.viewState

import androidx.compose.runtime.Immutable

@Immutable
class ComposeList<out T>(
    private val value: List<T>
): List<T> by value

operator fun <T> ComposeList<T>.plus(element: T): ComposeList<T> =
    this.toMutableList().apply {
        add(element)
    }.toComposeList()

operator fun <T> ComposeList<T>.minus(element: T): ComposeList<T> =
    this.toMutableList().apply {
        remove(element)
    }.toComposeList()

fun <T> composeListOf(vararg elements: T): ComposeList<T> =
    ComposeList(listOf(*elements))

fun <T> emptyComposeList(): ComposeList<T> = ComposeList(emptyList())

fun <T> List<T>.toComposeList(): ComposeList<T> = ComposeList(this)

fun <T> Array<T>.toComposeList(): ComposeList<T> = ComposeList(this.asList())
