package com.nightx.ingale.root.impl.root.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nightx.ingale.core.ui.AnimatedChildSlot
import com.nightx.ingale.core.ui.ScreenTransitionDuration
import com.nightx.ingale.core.ui.slideInFromBottom
import com.nightx.ingale.core.ui.slideInFromLeftOutToRight
import com.nightx.ingale.core.ui.slideInFromRightOutToLeft
import com.nightx.ingale.core.ui.slideOutToBottom
import com.nightx.ingale.featureLocal.navigation.api.LocalComponent
import com.nightx.ingale.featureLocal.navigation.ui.LocalScreen
import com.nightx.ingale.globalPlaybackPresentation.musicBar.ui.MusicBar
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.ui.PlaybackScreen
import com.nightx.ingale.root.api.root.RootComponent
import com.nightx.ingale.root.api.root.RootScreenConfig
import com.nightx.ingale.root.api.root.toBottomBarItemViewState
import com.nightx.ingale.root.impl.root.ui.components.BottomBar
import com.nightx.ingale.root.impl.root.ui.components.BottomBarHeight
import com.nightx.ingale.root.impl.snackbar.uiComponents.SnackbarView

@Composable
fun RootScreen(
    rootComponent: RootComponent,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        BottomContent(
            rootComponent = rootComponent,
            screenContent = {
                BottomTabScreen(rootComponent.childStack)
            }
        )
    }
}

@OptIn(FaultyDecomposeApi::class)
@Composable
private fun BottomTabScreen(
    childStack: Value<ChildStack<RootScreenConfig, Any>>
) {
    Children(
        modifier = Modifier.fillMaxSize(),
        stack = childStack,
        animation = stackAnimation { child, otherChild, direction ->
            getContentTransformation(child.configuration, otherChild.configuration, direction)
        }
    ) {
        when (val childComponent = it.instance) {
            is LocalComponent -> {
                LocalScreen(childComponent)
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

// Not passing BottomTabScreen from outside causes unnecessary recompositions
// for RootScreen, BottomTabScreen, LocalScreen on every frame of BottomBar
// appearance/disappearance animation
@Composable
private fun BoxScope.BottomContent(
    rootComponent: RootComponent,
    screenContent: @Composable () -> Unit,
) {
    val state by rootComponent.uiState.collectAsState()
    val bottomBarOffset = remember(state.isVisible) {
        if (state.isVisible) {
            0.dp
        } else {
            BottomBarHeight
        }
    }

    val bottomBarOffsetAnim by animateDpAsState(
        label = "bottom bar show/hide animation",
        targetValue = bottomBarOffset,
        animationSpec = tween(ScreenTransitionDuration)
    )

    var isPlaybackScreenVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(bottom = BottomBarHeight - bottomBarOffsetAnim)
            .zIndex(BottomTabScreenZ)
    ) {
        screenContent()
    }
    AnimatedChildSlot(
        modifier = Modifier.zIndex(PlaybackScreenZ),
        slot = rootComponent.childSlot,
        enterAnimation = slideInFromBottom(),
        exitAnimation = slideOutToBottom(),
    ) { child ->
        PlaybackScreen(child.instance)
        DisposableEffect(Unit) {
            isPlaybackScreenVisible = true
            onDispose {
                isPlaybackScreenVisible = false
            }
        }
    }
    MusicBar(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .graphicsLayer {
                translationY = -(BottomBarHeight.toPx() - bottomBarOffsetAnim.toPx())
            }
            .zIndex(MusicBarZ),
        component = rootComponent.musicBarComponent,
    )
    SnackbarView(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(
                if (isPlaybackScreenVisible) {
                    SnackbarZWithPlaybackScreen
                } else {
                    SnackbarZWithoutPlaybackScreen
                }
            ),
        bottomBarPadding = {
            BottomBarHeight - bottomBarOffsetAnim
        },
        snackbarComponent = rootComponent.snackbarComponent,
    )
    BottomBar(
        modifier = Modifier.zIndex(BottomBarZ),
        offset = {
            bottomBarOffsetAnim.roundToPx()
        },
        selectedTab = state.selectedTab,
        onTabSelected = rootComponent::onTabSelected,
    )
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

private const val BottomTabScreenZ = 1f
private const val MusicBarZ = 2f
private const val SnackbarZWithoutPlaybackScreen = 3f

private const val BottomBarZ = 4f

private const val PlaybackScreenZ = 5f

private const val SnackbarZWithPlaybackScreen = 6f
