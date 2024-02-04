package com.example.ingale.feature_local.songs_set.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.solver.widgets.Optimizer
import com.example.ingale.R
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.feature_local.local_core.presentation.SongsLazyList
import com.example.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.example.ingale.feature_local.songs_set.presentation.view.LocalSongsSetContract
import com.example.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import com.example.ingale.main_navigation.bottom_bar_controls.SendBottomBarEffect
import kotlin.math.abs

@Composable
fun SongsSetScreen(
    state: LocalSongsSetContract.State,
    sendEvent: (LocalSongsSetContract.Event) -> Unit,
) {
    SendBottomBarEffect(BottomBarEffect.HideBottomBar)
    val lazyListState = rememberLazyListState()
    val animationEndIndex = remember { 2 }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CollapsingTitle(
            songsSetInfo = state.songsSetInfo,
            progress = {
                if (lazyListState.layoutInfo.visibleItemsInfo.isEmpty()) 0f else
                if (lazyListState.firstVisibleItemIndex in (0..lazyListState.firstVisibleItemScrollOffset)) {
                    ((lazyListState.firstVisibleItemIndex + lazyListState.firstVisibleItemScrollOffset
                        .toFloat() / lazyListState.layoutInfo.visibleItemsInfo[0].size) * 100f / animationEndIndex).coerceIn(
                        0f,
                        100f
                    ) / 100
                } else 1f
            }
        )
        SongsLazyList(
            modifier = Modifier.fillMaxSize(),
            lazyListState = lazyListState,
            songs = state.songsSetInfo.songs,
            isLoading = false,
            onSongClick = {
                sendEvent(LocalSongsSetContract.Event.PlaySong(it))
            }
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun CollapsingTitle(
    songsSetInfo: SongsSet,
    progress: () -> Float,
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.songs_set_motion_scene)
            .readBytes()
            .decodeToString()
    }
    var boxHeight by remember {
        mutableStateOf(0.dp)
    }
    var mlHeight by remember {
        mutableStateOf(0.dp)
    }
    val icon = remember { songsSetInfo.icon }
    val density = LocalDensity.current
    Column {
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            progress = progress(),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    mlHeight = density.run {
                        it.boundsInParent().height.toDp()
                    }
                },
            optimizationLevel = Optimizer.OPTIMIZATION_DIRECT
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inversePrimary)
                .layoutId("background")
                .onSizeChanged {
                    boxHeight = density.run {
                        it.height.toDp()
                    }
                }
            )
            SongIcon(
                picture = icon,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .layoutId("songs_icon")
            )
            Text(
                text = songsSetInfo.title,
                fontSize = 24.sp,
                modifier = Modifier
                    .layoutId("songs_title")
            )
        }
        Spacer(modifier = Modifier.height(abs(boxHeight.value - mlHeight.value).dp))
    }
}