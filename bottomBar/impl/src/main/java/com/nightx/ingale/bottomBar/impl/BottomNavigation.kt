package com.nightx.ingale.bottomBar.impl

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nightx.ingale.bottomBar.api.LocalBottomBarController
import com.nightx.ingale.bottomBar.api.LocalBottomBarState
import com.nightx.ingale.bottomBar.api.LocalBottomTabActivityState
import com.nightx.ingale.bottomBar.impl.bottomBar.BottomBarItem
import com.nightx.ingale.bottomBar.impl.bottomBar.uiComponents.BottomBarItem
import com.nightx.ingale.bottomBar.impl.bottomBar.uiComponents.SlidingBottomTabSwitcher
import com.nightx.ingale.bottomBar.impl.snackbar.uiComponents.SnackbarHost
import com.nightx.ingale.core.ui.LaunchedEffect
import com.nightx.ingale.core.ui.screenTransitionDuration
import com.nightx.ingale.globalPlaybackPresentation.ui.MusicBar
import com.nightx.ingale.globalPlaybackPresentation.ui.PlaybackScreen
import com.nightx.ingalefeatureLocal.navigation.graph.LocalSectionNavGraph

@Composable
fun BottomNavigation() {
    var isPlaybackScreenVisible by remember { mutableStateOf(false) }
    val isBottomBarVisible by LocalBottomBarState.current.isBottomBarVisible.collectAsState()

    val snackbarLayer by remember(isPlaybackScreenVisible) {
        mutableFloatStateOf(
            if (isPlaybackScreenVisible) {
                SnackbarZWithPlaybackScreen
            } else {
                SnackbarZWithoutPlaybackScreen
            }
        )
    }

    val bottomBarOffset = remember(isBottomBarVisible) {
        if (isBottomBarVisible) {
            0.dp
        } else {
            BottomBarHeight
        }
    }

    val bottomBarOffsetAnim by animateDpAsState(
        label = "bottom bar hiding and showing",
        targetValue = bottomBarOffset,
        animationSpec = tween(screenTransitionDuration)
    )

    var selected by rememberSaveable {
        mutableStateOf(BottomBarItem.Local)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BottomBar(
            modifier = Modifier.zIndex(BottomBarZ),
            offset = {
                bottomBarOffsetAnim.roundToPx()
            },
            onTabSelected = {
                selected = it
            }
        )
        Box(
            modifier = Modifier
                .zIndex(RootNavigationZ)
                .fillMaxSize(1f)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(bottom = BottomBarHeight - bottomBarOffsetAnim)
        ) {
            RootNavigation(selected)
        }
        SnackbarView(
            modifier = Modifier
                .zIndex(snackbarLayer)
                .align(Alignment.BottomCenter),
            bottomBarPadding = {
                BottomBarHeight - bottomBarOffsetAnim
            }
        )
        MusicBar(
            modifier = Modifier
                .zIndex(MusicBarZ)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = -(BottomBarHeight.toPx() - bottomBarOffsetAnim.toPx())
                }
        )
        PlaybackScreen(
            modifier = Modifier
                .zIndex(PlaybackScreenZ),
            onVisibilityChanged = {
                isPlaybackScreenVisible = it
            }
        )
    }
}

@Composable
private fun BoxScope.BottomBar(
    modifier: Modifier = Modifier,
    offset: Density.() -> Int,
    onTabSelected: (BottomBarItem) -> Unit,
) {
    val isBottomBarVisible by LocalBottomBarState.current.isBottomBarVisible.collectAsState()

    var selected by rememberSaveable {
        mutableStateOf(BottomBarItem.Local)
    }

    BackHandler {
        selected = BottomBarItem.Local
        onTabSelected(selected)
    }

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(BottomBarHeight)
            .align(Alignment.BottomCenter)
            .offset {
                IntOffset(
                    x = 0,
                    y = offset()
                )
            },
        containerColor = MaterialTheme.colorScheme.inversePrimary
    ) {
        BottomBarItem.entries.forEach { item ->
            BottomBarItem(
                item = item,
                isSelected = selected == item,
                onClick = {
                    if (isBottomBarVisible) {
                        selected = item
                        onTabSelected(selected)
                    }
                }
            )
        }
    }
}

@Composable
private fun SnackbarView(
    modifier: Modifier = Modifier,
    bottomBarPadding: () -> Dp,
) {
    val keyboardHeight by keyboardHeight()
    val snackbarBottomPadding by remember {
        derivedStateOf {
            if (keyboardHeight > BottomBarHeight) {
                keyboardHeight
            } else {
                bottomBarPadding()
            }
        }
    }
    SnackbarHost(
        modifier = modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = -snackbarBottomPadding.roundToPx()
                )
            }
    )
}

@Composable
private fun keyboardHeight(): State<Dp> {
    val keyboardHeight = LocalDensity.current
        .run {
            WindowInsets.ime.getBottom(this).toDp()
        }
    return rememberUpdatedState(keyboardHeight)
}

@Composable
private fun RootNavigation(
    selectedTab: BottomBarItem,
) {
    SlidingBottomTabSwitcher(
        selectedTab = selectedTab
    ) { currentTab ->
        CompositionLocalProvider(
            LocalBottomTabActivityState provides (selectedTab == currentTab)
        ) {
            when (currentTab) {
                BottomBarItem.Local -> LocalSectionNavGraph()
                BottomBarItem.Youtube -> YouTubeSection()
            }
        }
    }
}

@Composable
private fun YouTubeSection() {
    val tabActivityState = LocalBottomTabActivityState.current
    val bottomBarController = LocalBottomBarController.current
    LaunchedEffect(tabActivityState) { isTabActive ->
        if (isTabActive) {
            bottomBarController.setVisibility(true)
        }
    }

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

internal val BottomBarHeight = 80.dp

private const val RootNavigationZ = 1f
private const val MusicBarZ = 2f
private const val SnackbarZWithoutPlaybackScreen = 3f
private const val BottomBarZ = 4f
private const val PlaybackScreenZ = 5f
private const val SnackbarZWithPlaybackScreen = 6f
