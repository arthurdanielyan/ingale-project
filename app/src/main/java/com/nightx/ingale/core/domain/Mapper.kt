package com.nightx.ingale.core.domain

interface Mapper<F, T> {

    operator fun invoke(from: F): T
}

fun <F, T> Mapper<F, T>.mapList(list: List<F>): List<T> =
    list.map(this::invoke)
