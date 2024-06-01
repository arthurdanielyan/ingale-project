package com.nightx.ingale.main_navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.presentation.ui.AutoSizeText
import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalSectionNavGraph
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffect
import com.nightx.ingale.main_navigation.bottomBarControls.LocalBottomBarEffects
import com.nightx.ingale.main_navigation.musicBar.MusicBar
import com.nightx.ingale.ui.theme.dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
        animationSpec = tween(300)
    )

    ComposeCollect(
        flow = LocalBottomBarEffects.current.bottomBarEffect,
        latest = true
    ) {
        when (it) {
            BottomBarEffect.ShowBottomBar -> isBottomBarVisible = true
            BottomBarEffect.HideBottomBar -> isBottomBarVisible = false
            BottomBarEffect.CollapseMusicBar -> {}
            BottomBarEffect.ExpandMusicBar -> {}
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem.Local,
        BottomNavItem.Youtube
    )
    var selected by rememberSaveable {
        mutableIntStateOf(BottomNavItem.Local.titleKey)
    }
    val pagerState = rememberPagerState(
        pageCount = { bottomNavItems.size }
    )
    val scope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(BottomBarHeight)
                    .offset(y = bottomBarOffsetAnim),
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ) {
                bottomNavItems.forEachIndexed { index, item ->
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
                key = { bottomNavItems[it].titleKey },
                userScrollEnabled = false,
            ) {
                when (it) {
                    0 -> {
                        LocalSectionNavGraph()
                    }

                    1 -> {
                        Box(
                            modifier = Modifier
                                .requiredSize(100.dp)
                                .background(color = Color.Gray)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "veow kbjren ij ewv 3ijjwg fv3iuewkg f3 fi32u gfeo2u f2qig f2oeg v3woieg 2og o23 goe goe goew",
                                maxLines = 1,
                                softWrap = false
                            )
                            AutoSizeText(
                                modifier = Modifier.fillMaxWidth(),
                                text = "veow kbjren ij ewv 3ijjwg fv3iuewkg f3 fi32u gfeo2u f2qig f2oeg v3woieg 2og o23 goe goe goew",
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            MusicBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

private val BottomBarHeight = 80.dp
