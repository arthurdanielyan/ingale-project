package com.nightx.ingale.bottomBar.impl

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nightx.ingale.bottomBar.api.LocalBottomBarController
import com.nightx.ingale.bottomBar.api.LocalBottomBarState
import com.nightx.ingale.bottomBar.api.LocalBottomTabActivityState
import com.nightx.ingale.bottomBar.impl.bottomBar.BottomBarItem
import com.nightx.ingale.bottomBar.impl.bottomBar.uiComponents.BottomBarItem
import com.nightx.ingale.bottomBar.impl.bottomBar.uiComponents.SlidingBottomTabSwitcher
import com.nightx.ingale.bottomBar.impl.snackbar.uiComponents.SnackbarHost
import com.nightx.ingale.core.ui.LaunchedEffect
import com.nightx.ingale.core.ui.extensions.copy
import com.nightx.ingale.core.ui.screenTransitionDuration
import com.nightx.ingale.musicbar.MusicBar
import com.nightx.ingalefeatureLocal.navigation.graph.LocalSectionNavGraph

@Composable
fun BottomNavigation() {
    val isBottomBarVisible by LocalBottomBarState.current.isBottomBarVisible.collectAsState()

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

    BackHandler {
        selected = BottomBarItem.Local
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(BottomBarHeight)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = bottomBarOffsetAnim.roundToPx()
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
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(
                    innerPadding.copy(
                        bottom = BottomBarHeight - bottomBarOffsetAnim
                    )
                )
        ) {
            RootNavigation(selected)
            MusicBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
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
