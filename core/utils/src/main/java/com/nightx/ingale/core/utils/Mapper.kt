package com.nightx.ingale.core.utils

interface Mapper<F, T> {

    fun map(from: F): T

    operator fun invoke(from: F): T = map(from)
}

fun <F, T> Mapper<F, T>.mapList(list: List<F>): List<T> =
    list.map(this::map)
