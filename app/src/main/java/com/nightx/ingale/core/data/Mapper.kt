package com.nightx.ingale.core.data

interface Mapper<F, T> {

    fun map(from: F): T
}

fun <F, T> Mapper<F, T>.mapList(list: List<F>): List<T> =
    list.map(::map)
