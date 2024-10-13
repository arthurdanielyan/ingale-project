package com.nightx.ingale.bottomBar.impl

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.nightx.ingale.bottomBar.api.BottomBarEffect
import com.nightx.ingale.bottomBar.api.LocalBottomBarEffects
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.ui.AutoSizeText
import com.nightx.ingale.core.ui.screenTransitionDuration
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.musicbar.MusicBar
import com.nightx.ingalefeatureLocal.navigation.graph.LocalSectionNavGraph
import kotlinx.coroutines.launch

@Composable
fun BottomNavigation() {
    var isBottomBarVisible by remember {
        mutableStateOf(true)
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

    ComposeCollect(
        flow = LocalBottomBarEffects.current.bottomBarEffect,
        latest = true
    ) {
        isBottomBarVisible = when (it) {
            BottomBarEffect.ShowBottomBar -> true
            BottomBarEffect.HideBottomBar -> false
        }
    }

    val bottomBarItems = rememberSaveable(saver = BottomBarItem.ListSaver) {
        listOf(
            BottomBarItem.Local,
            BottomBarItem.Youtube
        )
    }
    var selected by rememberSaveable {
        mutableIntStateOf(BottomBarItem.Local.titleKey)
    }
    val pagerState = rememberPagerState(
        pageCount = { bottomBarItems.size }
    )
    val scope = rememberCoroutineScope()
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
                bottomBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.inversePrimary,
                            selectedTextColor = MaterialTheme.colorScheme.inverseSurface,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.requiredSize(1.15 * BottomBarHeight),
                        selected = selected == item.titleKey,
                        onClick = {
                            if (isBottomBarVisible) {
                                selected = item.titleKey
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp),
                                painter = painterResource(item.icon),
                                contentDescription = stringResource(item.titleKey)
                            )
                        },
                        label = {
                            MaterialTheme.dimensions.normal
                            Text(
                                text = stringResource(item.titleKey),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
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
                    top = innerPadding.calculateTopPadding(),
                    bottom = BottomBarHeight - bottomBarOffsetAnim
                )
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                key = { bottomBarItems[it].titleKey },
                beyondViewportPageCount = 1,
                userScrollEnabled = false,
            ) {
                when (it) {
                    0 -> LocalSectionNavGraph()
                    1 -> SecondTab()
                }
            }
            MusicBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

// Temporary
@Composable
private fun SecondTab() {
    Box(
        modifier = Modifier
            .requiredSize(100.dp)
            .background(color = Color.Gray)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            maxLines = 1,
            softWrap = false
        )
        AutoSizeText(
            modifier = Modifier.fillMaxWidth(),
            text = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            maxLines = 1
        )
    }
}

private val BottomBarHeight = 80.dp
