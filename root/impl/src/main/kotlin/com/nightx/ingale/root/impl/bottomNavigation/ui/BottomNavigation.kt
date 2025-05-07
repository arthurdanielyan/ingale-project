package com.nightx.ingale.root.impl.bottomNavigation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.screenTransitionDuration
import com.nightx.ingale.globalPlaybackPresentation.musicBar.ui.MusicBar
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.api.bottomBar.BottomNavigationComponent
import com.nightx.ingale.root.impl.bottomNavigation.ui.components.BottomBarItem
import com.nightx.ingale.root.impl.snackbar.uiComponents.SnackbarView

@Composable
fun BottomNavigation(
    component: BottomNavigationComponent,
    content: @Composable () -> Unit
) {
    val state by component.uiState.collectAsState()

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
        animationSpec = tween(screenTransitionDuration)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(bottom = BottomBarHeight - bottomBarOffsetAnim)
        ) {
            content()
        }
        MusicBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = -(BottomBarHeight.toPx() - bottomBarOffsetAnim.toPx())
                },
            component = component.musicBarComponent,
        )
        SnackbarView(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            bottomBarPadding = {
                BottomBarHeight - bottomBarOffsetAnim
            },
            snackbarComponent = component.snackbarComponent,
        )
        BottomBar(
            offset = {
                bottomBarOffsetAnim.roundToPx()
            },
            selectedTab = state.selectedTab,
            onTabSelected = component::onTabSelected,
        )
    }
}

@Composable
private fun BoxScope.BottomBar(
    modifier: Modifier = Modifier,
    offset: Density.() -> Int,
    selectedTab: BottomBarItemViewState,
    onTabSelected: (BottomBarItemViewState) -> Unit,
) {
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
        BottomBarItemViewState.entries.forEach { item ->
            BottomBarItem(
                item = item,
                isSelected = selectedTab == item,
                onClick = {
                    onTabSelected(item)
                }
            )
        }
    }
}

internal val BottomBarHeight = 80.dp

private const val RootNavigationZ = 1f
private const val MusicBarZ = 2f
private const val SnackbarZWithoutPlaybackScreen = 3f
private const val BottomBarZ = 4f
private const val PlaybackScreenZ = 5f
private const val SnackbarZWithPlaybackScreen = 6f
