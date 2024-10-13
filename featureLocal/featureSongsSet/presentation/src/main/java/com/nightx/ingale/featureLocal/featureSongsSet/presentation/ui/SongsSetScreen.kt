package com.nightx.ingale.featureLocal.featureSongsSet.presentation.ui

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.ingale.bottomBar.api.BottomBarEffect
import com.nightx.ingale.bottomBar.api.SetBottomBarState
import com.nightx.ingale.core.presentation.diExt.ingaleViewModels
import com.nightx.ingale.core.ui.SongIcon
import com.nightx.ingale.featureLocal.core.ui.SongsLazyList
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.R
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel.SongsSetCallbacks
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel.SongsSetViewModel
import com.nightx.ingale.musicbar.view.rememberNestedScrollForMusicBarNotification

@Composable
fun SongsSetScreen(savedStateHandle: SavedStateHandle) {
    val vm = ingaleViewModels<SongsSetViewModel>(
        savedStateHandle = savedStateHandle
    )
    val state by vm.state.collectAsStateWithLifecycle()

    SetBottomBarState(BottomBarEffect.HideBottomBar)

    SongsSetScreen(
        state = state,
        callbacks = vm
    )
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun SongsSetScreen(
    state: SongsSetViewState,
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
            songs = state.songs,
            isLoading = false,
            onSongClick = callbacks::onSongClick,
        )
        SongsSetHeader(
            icon = state.iconPath,
            title = state.title
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
