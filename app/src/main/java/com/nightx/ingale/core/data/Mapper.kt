package com.nightx.ingale.core.data

interface Mapper<DOMAIN, DATA> {

    fun mapToData(from: DOMAIN): DATA

    fun mapToDomain(from: DATA): DOMAIN
}

fun <DATA, DOMAIN> Mapper<DOMAIN, DATA>.mapToDomainList(list: List<DATA>): List<DOMAIN> =
    list.map {
        mapToDomain(it)
    }

fun <DATA, DOMAIN> Mapper<DOMAIN, DATA>.mapToDataList(list: List<DOMAIN>): List<DATA> =
    list.map {
        mapToData(it)
    }