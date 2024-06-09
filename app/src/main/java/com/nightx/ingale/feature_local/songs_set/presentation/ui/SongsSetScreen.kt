package com.nightx.ingale.feature_local.songs_set.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutScope
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.solver.widgets.Optimizer
import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.R
import com.nightx.ingale.core.presentation.di.ingaleViewModels
import com.nightx.ingale.core.presentation.view.rememberNestedScrollForMusicBarNotification
import com.nightx.ingale.feature_local.local_core.presentation.SongsLazyList
import com.nightx.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.LocalSongsSetContract
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.SongsSetCallbacks
import com.nightx.ingale.feature_local.songs_set.presentation.view.viewModel.SongsSetViewModel
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffect
import com.nightx.ingale.main_navigation.bottomBarControls.SendBottomBarEffect

@Composable
fun SongsSetScreen(savedStateHandle: SavedStateHandle) {
    val vm = ingaleViewModels<SongsSetViewModel>(
        savedStateHandle = savedStateHandle
    )

    SendBottomBarEffect(BottomBarEffect.HideBottomBar)
    SongsSetScreen(
        state = vm.state.collectAsState().value,
        callbacks = vm
    )
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun SongsSetScreen(
    state: LocalSongsSetContract.State,
    callbacks: SongsSetCallbacks,
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.songs_set_motion_scene)
            .readBytes()
            .decodeToString()
    }

    val lazyListState = rememberLazyListState()
    val progress by rememberProgress(
        lazyListState = lazyListState,
        collapseEndItemIndex = CollapseEndItemIndex,
    )
    val musicBarNotifier = rememberNestedScrollForMusicBarNotification()

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier
            .fillMaxSize(),
        optimizationLevel = Optimizer.OPTIMIZATION_DIRECT
    ) {
        SongsLazyList(
            lazyListState = lazyListState,
            modifier = Modifier
                .layoutId("content")
                .nestedScroll(musicBarNotifier),
            songs = state.songsSetInfo.songs,
            isLoading = false,
            onSongClick = callbacks::onSongClick,
        )
        SongsSetHeader(
            icon = state.songsSetInfo.iconPath,
            title = state.songsSetInfo.title
        )
    }
}

@Composable
private fun MotionLayoutScope.SongsSetHeader(
    icon: String?,
    title: String,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inversePrimary)
            .layoutId("header_background")
    )
    SongIcon(
        model = icon,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .layoutId("songs_icon")
    )
    Text(
        text = title,
        fontSize = 24.sp,
        modifier = Modifier
            .layoutId("songs_title")
    )
}

@Composable
private fun rememberProgress(
    lazyListState: LazyListState,
    collapseEndItemIndex: Int,
) = remember {
    derivedStateOf {
        if (lazyListState.layoutInfo.visibleItemsInfo.isEmpty()) {
            0f
        } else {
            if (lazyListState.firstVisibleItemIndex in (0..collapseEndItemIndex)) {
                ((lazyListState.firstVisibleItemIndex + lazyListState.firstVisibleItemScrollOffset
                    .toFloat() / lazyListState.layoutInfo.visibleItemsInfo[0].size) * 100f / collapseEndItemIndex).coerceIn(
                    0f,
                    100f
                ) / 100
            } else { // firstVisibleItemIndex > collapseEndItemIndex
                1f
            }
        }
    }
}

private const val CollapseEndItemIndex = 2
