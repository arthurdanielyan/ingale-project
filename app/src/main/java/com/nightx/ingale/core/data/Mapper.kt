package com.nightx.ingale.core.data

interface Mapper<DOMAIN, DATA> {

    fun mapToData(from: DOMAIN): DATA

    fun mapToDomain(from: DATA): DOMAIN
}