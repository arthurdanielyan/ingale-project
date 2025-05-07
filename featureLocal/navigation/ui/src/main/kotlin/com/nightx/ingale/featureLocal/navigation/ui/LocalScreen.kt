package com.nightx.ingale.featureLocal.navigation.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.LocalMainScreen
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.api.SongsSetComponent
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.ui.SongsSetScreen
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent

@Composable
fun LocalScreen(
    component: LocalComponent,
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide()),
    ) {
        when (val childComponent = it.instance) {
            is LocalHomeComponent -> LocalMainScreen(childComponent)
            is SongsSetComponent -> SongsSetScreen(childComponent)
        }
    }
}