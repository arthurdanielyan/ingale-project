package com.nightx.ingale.featureLocal.navigation.impl

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.LocalMainScreen
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent

@Composable
fun LocalScreen(
    component: LocalComponent,
) {
    Children(
        stack = component.childStack
    ) {
        when (val childComponent = it.instance) {
            is LocalHomeComponent -> LocalMainScreen(childComponent)
            else -> Unit
        }
    }
}