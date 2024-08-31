package com.nightx.ingale.resources.strings

interface StringProvider {

    fun string(resId: Int, vararg args: Any): String
}