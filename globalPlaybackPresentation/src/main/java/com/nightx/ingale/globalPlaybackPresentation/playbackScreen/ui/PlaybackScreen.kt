package com.nightx.ingale.globalPlaybackPresentation.playbackScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import coil.compose.AsyncImage
import com.nightx.ingale.core.ui.MusicProgressIndicator
import com.nightx.ingale.core.ui.TextMarquee
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenComponent
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenUiCallbacks
import com.nightx.ingale.globalPlaybackPresentation.playbackScreen.api.PlaybackScreenViewState
import com.nightx.ingale.resources.icon.R

@Composable
fun PlaybackScreen(
    component: PlaybackScreenComponent,
) {
    val state by component.uiState.collectAsState()
    val callbacks = component.uiCallbacks

    PlaybackScreen(
        state = state,
        playbackProgress = component.playbackProgress.collectAsState(),
        callbacks = callbacks
    )
}

@Composable
private fun PlaybackScreen(
    state: PlaybackScreenViewState,
    playbackProgress: State<Float>,
    callbacks: PlaybackScreenUiCallbacks,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .clickable(
                enabled = false,
                onClick = {}
            ) // disable click-through
    ) {
        IconButton(
            modifier = Modifier
                .requiredSize(ButtonSize),
            onClick = callbacks::onClose,
        ) {
            Icon(
                modifier = Modifier
                    .requiredSize(IconSize)
                    .padding(MaterialTheme.dimensions.normal),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextMarquee(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                text = state.songName,
            )
            SongPreview(
                modifier = Modifier
                    .fillMaxWidth(0.4f),
                preview = state.currentSongPreviewPath,
            )
            MusicProgressIndicator(
                progress = playbackProgress,
                onProgressChange = callbacks::onSeekTo,
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