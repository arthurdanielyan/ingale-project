package com.nightx.ingale.featureLocal.navigation.api

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nightx.ingale.core.decompose.AppComponentContext

interface LocalComponent {

    val childStack: Value<ChildStack<LocalScreenConfig, Any>>

    fun interface Factory {

        operator fun invoke(
            appComponentContext: AppComponentContext
        ): LocalComponent
    }
}