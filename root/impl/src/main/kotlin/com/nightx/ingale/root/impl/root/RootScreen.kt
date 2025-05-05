package com.nightx.ingale.root.impl.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.nightx.ingale.core.ui.slideInFromLeftOutToRight
import com.nightx.ingale.core.ui.slideInFromRightOutToLeft
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.root.RootScreenConfig
import com.nightx.ingale.root.api.root.toBottomBarItemViewState
import com.nightx.ingale.root.impl.bottomNavigation.ui.BottomNavigation

@OptIn(FaultyDecomposeApi::class)
@Composable
fun RootScreen(
    rootComponent: RootComponent,
) {
    BottomNavigation(
        component = rootComponent.bottomNavigationComponent
    ) {
        Children(
            modifier = Modifier.fillMaxSize(),
            stack = rootComponent.childStack,
            animation = stackAnimation { child, otherChild, direction ->
                getContentTransformation(child.configuration, otherChild.configuration, direction)
            }
        ) {
            when (it.instance) {
                is LocalComponent -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Local Tab",
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Not Implemented Yet. Wait for 1000 centuries...",
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private fun getContentTransformation(
    initialConfig: RootScreenConfig,
    targetConfig: RootScreenConfig,
    direction: Direction
): StackAnimator {
    return if (initialConfig.toBottomBarItemViewState().ordinal < targetConfig.toBottomBarItemViewState().ordinal) {
        when (direction) {
            Direction.ENTER_BACK,
            Direction.ENTER_FRONT -> slideInFromLeftOutToRight()

            Direction.EXIT_BACK,
            Direction.EXIT_FRONT -> slideInFromRightOutToLeft()
        }
    } else {
        when (direction) {
            Direction.ENTER_BACK,
            Direction.ENTER_FRONT -> slideInFromRightOutToLeft()

            Direction.EXIT_BACK,
            Direction.EXIT_FRONT -> slideInFromLeftOutToRight()
        }
    }
}
