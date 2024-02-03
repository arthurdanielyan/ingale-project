package com.example.ingale.main_navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.ingale.core.presentation.flows.ComposeCollect
import com.example.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.example.ingale.feature_local.local_navigation.screen_navigation.LocalSectionNavGraph
import com.example.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import com.example.ingale.main_navigation.bottom_bar_controls.LocalBottomBarEffects
import com.example.ingale.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNavigation() {

    var bottomBarOffset by remember { mutableStateOf(0.dp) }
    val bottomBarOffsetAnim by animateDpAsState(
        label = "bottom bar hiding and showing",
        targetValue = bottomBarOffset,
        animationSpec = tween(300)
    )

    val hideBottomBar = {
        bottomBarOffset = BottomBarHeight
    }
    val showBottomBar = {
        bottomBarOffset = 0.dp
    }
    ComposeCollect(
        flow = LocalBottomBarEffects.current.bottomBarEffect,
        latest = true
    ) {
        when (it) {
            BottomBarEffect.ShowBottomBar -> showBottomBar()
            BottomBarEffect.HideBottomBar -> hideBottomBar()
            BottomBarEffect.CollapseMusicInfo -> {}
            BottomBarEffect.ExpandMusicInfo -> {}
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem.Local,
        BottomNavItem.Youtube
    )
    var selected by rememberSaveable {
        mutableStateOf(BottomNavItem.Local.title)
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
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier.requiredSize(1.15 * BottomBarHeight),
                        selected = selected == item.title,
                        onClick = {
                            selected = item.title
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp),
                                painter = painterResource(item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = {
                            val offset = MaterialTheme.spacing.normal
                            Text(
                                text = item.title,
                                modifier = Modifier.offset {
                                    IntOffset(0, offset.roundToPx())
                                }
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
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = BottomBarHeight - bottomBarOffsetAnim
                )
        ) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    key = { bottomNavItems[it].title },
                    userScrollEnabled = false
                ) {
                    when (it) {
                        0 -> {
                            LocalSectionNavGraph()
                        }

                        1 -> {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = "Youtube"
                            )
                        }
                    }
                }
                CurrentMusicInfoButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun CurrentMusicInfoButton(
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(true) }
    var fullWidth by remember { mutableStateOf(MusicInfoButtonHeight) }
    val cardWidth by animateDpAsState(
        targetValue = if(isExpanded) fullWidth else MusicInfoButtonHeight,
        animationSpec = tween(durationMillis = MusicInfoButtonCollapsingDuration),
        label = "music card width animation"
    )

    ComposeCollect(
        flow = LocalBottomBarEffects.current.bottomBarEffect,
        latest = true
    ) {
        when(it) {
            BottomBarEffect.ExpandMusicInfo -> {
                isExpanded = true
            }
            BottomBarEffect.CollapseMusicInfo -> {
                isExpanded = false
            }
            BottomBarEffect.HideBottomBar -> {}
            BottomBarEffect.ShowBottomBar -> {}
        }
    }

    val infiniteTransition = rememberInfiniteTransition(
        label = "Music image rotation"
    )
    val musicImageRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = MusicPreviewRotationDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Music image rotation"
    )
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.normal)
            .requiredHeight(MusicInfoButtonHeight)
    ) {
        fullWidth = maxWidth
        Column(
            modifier = Modifier
                .requiredWidth(cardWidth)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    shape = CircleShape
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.normal)
                    .graphicsLayer {
                        this.rotationZ = musicImageRotation.value
                    }
            ) {
                SongIcon(
                    picture = null
                )
            }
        }
    }
}

private val MusicInfoButtonHeight = 50.dp
private const val MusicInfoButtonCollapsingDuration = 500
private const val MusicPreviewRotationDuration = 5000

private val BottomBarHeight = 80.dp