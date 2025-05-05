package com.nightx.ingale.globalPlaybackPresentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.ui.TextMarquee
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.globalPlaybackPresentation.view.GlobalPlaybackComponent
import com.nightx.ingale.globalPlaybackPresentation.view.GlobalPlaybackViewCallbacks
import com.nightx.ingale.resources.icon.R
import org.koin.compose.koinInject

@Composable
fun PlaybackScreen(
    modifier: Modifier = Modifier,
    onVisibilityChanged: (Boolean) -> Unit = {},
) {
    val globalPlaybackComponent = koinInject<GlobalPlaybackComponent>()
    val state by globalPlaybackComponent.state.collectAsStateWithLifecycle()

    AnimatedVisibility(
        modifier = modifier,
        visible = state.isPlaybackScreenVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        // All these side effects should execute when the PlaybackScreen itself
        // enters the composition.
//        val bottomBarState = com.nightx.ingale.root.api.LocalBottomBarState.current
//        val bottomBarInitialState = remember {
//            bottomBarState.isBottomBarVisible.value
//        }
//        val bottomBarController = com.nightx.ingale.root.api.LocalBottomBarController.current
//
//        SingleLaunchedEffect {
//            Log.d("myLogs", "bottomBarInitialState: $bottomBarInitialState")
//            bottomBarController.setVisibility(false)
//        }
//        ObserveState(state.isPlaybackScreenVisible) { isPlaybackScreenVisible ->
//            bottomBarController.setVisibility(
//                if (isPlaybackScreenVisible) {
//                    false
//                } else {
//                    bottomBarInitialState
//                }
//            )
//        }

        DisposableEffect(Unit) {
            onVisibilityChanged(true)
            onDispose {
                onVisibilityChanged(false)
            }
        }

        PlaybackScreen(
            currentSong = state.currentSongInfo,
            callbacks = globalPlaybackComponent
        )
    }
}

@Composable
private fun PlaybackScreen(
    currentSong: CurrentSongInfo,
    callbacks: GlobalPlaybackViewCallbacks,
) {
    BackHandler(onBack = callbacks::onClosePlaybackScreenClick)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .clickable(enabled = false, onClick = {}) // disable click-through
    ) {
        IconButton(
            modifier = Modifier
                .requiredSize(ButtonSize),
            onClick = callbacks::onClosePlaybackScreenClick,
        ) {
            Icon(
                modifier = Modifier
                    .requiredSize(IconSize),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TextMarquee(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                text = currentSong.songName,
            )
            SongPreview(
                modifier = Modifier
                    .fillMaxWidth(0.4f),
                preview = currentSong.currentSongPreviewPath,
            )
            LinearProgressIndicator(
                progress = { currentSong.seekPercentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimensions.large,
                        vertical = MaterialTheme.dimensions.normal,
                    ),

                )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimensions.large,
                        vertical = MaterialTheme.dimensions.normal,
                    )
            ) {

            }
        }
    }
}

@Composable
private fun SongPreview(
    modifier: Modifier = Modifier,
    preview: String?,
) {
    val density = LocalDensity.current
    var width by remember {
        mutableStateOf(100.dp)
    }
    if (preview.isNullOrBlank()) {
        Icon(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = "Song picture",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .onSizeChanged {
                    width = density.run {
                        it.width.toDp()
                    }
                }
                .height(width)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(MaterialTheme.dimensions.large)
                )
                .clip(RoundedCornerShape(MaterialTheme.dimensions.large))
        )
    } else {
        AsyncImage(
            model = preview,
            contentDescription = "Song picture",
            modifier = modifier
                .onSizeChanged {
                    width = density.run {
                        it.width.toDp()
                    }
                }
                .height(width)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(MaterialTheme.dimensions.large)
                )
                .clip(RoundedCornerShape(MaterialTheme.dimensions.large)),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.mipmap.ic_launcher_foreground),
        )
    }
}

private val ButtonSize = 40.dp
private val IconSize = 28.dp
