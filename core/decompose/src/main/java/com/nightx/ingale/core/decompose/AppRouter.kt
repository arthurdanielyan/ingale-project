package com.nightx.ingale.core.decompose

interface AppRouter {

    fun navigate(dest: ScreenConfig)

    fun pop()
}