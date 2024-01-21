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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ingale.feature_local.local_navigation.screen_navigation.LocalSectionNavGraph
import com.example.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.example.ingale.ui.theme.colorScheme.ingaleColors
import com.example.ingale.ui.theme.spacing
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed interface BottomNavigationEffects {
    data object ShowBottomBar : BottomNavigationEffects
    data object HideBottomBar : BottomNavigationEffects
}

val bottomNavigationEffects = MutableSharedFlow<BottomNavigationEffects>()
@Suppress("ObjectPropertyName")
private val _bottomNavigationEffects = bottomNavigationEffects.asSharedFlow()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNavigation() {

    val bottomBarHeight by remember { mutableStateOf(80.dp) }
    var bottomBarOffset by remember { mutableStateOf(0.dp) }
    val bottomBarOffsetAnim by animateDpAsState(
        label = "bottom bar hiding and showing",
        targetValue = bottomBarOffset,
        animationSpec = tween(300)
    )

    val hideBottomBar = {
        bottomBarOffset = bottomBarHeight
    }
    val showBottomBar = {
        bottomBarOffset = 0.dp
    }
    LaunchedEffect(Unit) {
        _bottomNavigationEffects.collect {
            when(it) {
                BottomNavigationEffects.ShowBottomBar -> showBottomBar()
                BottomNavigationEffects.HideBottomBar -> hideBottomBar()
            }
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
                    .requiredHeight(bottomBarHeight)
                    .offset(y = bottomBarOffsetAnim),
                containerColor = MaterialTheme.ingaleColors.primaryInverse,
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.ingaleColors.primaryInverse,
                            selectedTextColor = MaterialTheme.ingaleColors.backgroundInverse,
                            indicatorColor = MaterialTheme.ingaleColors.backgroundInverse,
                            unselectedIconColor = MaterialTheme.ingaleColors.onBackgroundLight,
                            unselectedTextColor = MaterialTheme.ingaleColors.onBackgroundLight
                        ),
                        selected = selected == item.title,
                        onClick = {
                            selected = item.title
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(45.dp),
                                painter = painterResource(item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title
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
                    bottom = bottomBarHeight - bottomBarOffsetAnim
                )
        ) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.ingaleColors.background)
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
fun currentMusicInfoButtonVerticalOccupation(): Dp =
    CurrentMusicInfoButtonHeight + CurrentMusicInfoButtonVerticalPadding

@Composable
private fun CurrentMusicInfoButton(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(
        label = "Music image rotation"
    )
    val musicImageRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Music image rotation"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.large,
                vertical = CurrentMusicInfoButtonVerticalPadding
            )
            .requiredHeight(CurrentMusicInfoButtonHeight)
            .background(
                color = MaterialTheme.ingaleColors.primaryInverse,
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

private val CurrentMusicInfoButtonVerticalPadding
    @Composable get() = MaterialTheme.spacing.normal
private val CurrentMusicInfoButtonHeight = 50.dp
