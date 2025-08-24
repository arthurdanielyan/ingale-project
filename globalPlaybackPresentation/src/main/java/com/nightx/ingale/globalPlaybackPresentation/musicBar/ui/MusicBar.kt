package com.nightx.ingale.globalPlaybackPresentation.musicBar.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.nightx.ingale.core.ui.PassiveIconButton
import com.nightx.ingale.core.ui.SongIcon
import com.nightx.ingale.core.ui.TextMarquee
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarComponent
import com.nightx.ingale.resources.playbackActions.R.drawable as PlaybackActions

@Composable
fun MusicBar(
    component: MusicBarComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.uiState.collectAsState()
    val callbacks = component.uiCallbacks

    var maxBarWidth by remember { mutableStateOf(MusicBarHeight) }
    val cardWidth by animateDpAsState(
        targetValue = if (state.isExpanded) maxBarWidth else MusicBarHeight,
        animationSpec = tween(durationMillis = MusicBarCollapsingDuration),
        label = "music bar width animation"
    )

    var marqueeContainerWidth by remember {
        mutableStateOf(0.dp)
    }

    val infiniteTransition = rememberInfiniteTransition(
        label = "Music image rotation"
    )
    val musicImageRotation by infiniteTransition.animateFloat(
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


    AnimatedVisibility(
        modifier = modifier,
        visible = state.isMusicBarVisible,
        enter = MusicBarAppearanceAnim,
        exit = MusicBarDisappearanceAnim
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.normal)
                .requiredHeight(MusicBarHeight)
                .clip(CircleShape)
        ) {
            maxBarWidth = this.maxWidth
            Row(
                modifier = Modifier
                    .requiredWidth(cardWidth)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable(
                        onClick = callbacks::onMusicBarClick
                    )
            ) {
                SongIcon(
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(MaterialTheme.dimensions.normal)
                        .size(MusicIconSize)
                        .graphicsLayer {
                            this.rotationZ = musicImageRotation
                        },
                    model = state.currentSongPreviewPath,
                )
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .zIndex(0f),
                ) marqueeTextContainer@{
                    if (marqueeContainerWidth < this.maxWidth) {
                        marqueeContainerWidth = this.maxWidth
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds(),
                        visible = state.isExpanded,
                        enter = MusicDetailsAppearanceAnim,
                        exit = MusicDetailsDisappearanceAnim,
                    ) musicDetailsAnim@{
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                alignment = Alignment.CenterVertically,
                                space = MaterialTheme.dimensions.small
                            )
                        ) {
                            TextMarquee(
                                text = state.songName,
                                modifier = Modifier.requiredWidth(marqueeContainerWidth),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            TextMarquee(
                                text = state.artistName,
                                modifier = Modifier
                                    .requiredWidth(marqueeContainerWidth),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimensions.large
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            start = MaterialTheme.dimensions.normal,
                            end = MaterialTheme.dimensions.large,
                        )
                ) {
                    val playPauseButton = remember(state.isPlaying) {
                        if (state.isPlaying) {
                            PlaybackActions.ic_pause
                        } else {
                            PlaybackActions.ic_play
                        }
                    }
                    PassiveIconButton(
                        painter = painterResource(playPauseButton),
                        contentDescription = "stop/resume music button",
                        onClick = callbacks::onTogglePlaybackClick,
                    )
                    PassiveIconButton(
                        painter = painterResource(PlaybackActions.ic_arrow_next),
                        contentDescription = "stop music button",
                        onClick = callbacks::onSkipToNextClick,
                    )
                }
            }
        }
    }
}

private val MusicBarHeight = 60.dp

private val MusicBarPadding: Dp
    @Composable get() = MaterialTheme.dimensions.normal

private val MusicIconSize: Dp
    @Composable get() = MusicBarHeight - 2 * MusicBarPadding

private const val MusicBarCollapsingDuration = 500
private const val MusicPreviewRotationDuration = 5000

private fun <T> getCommonAnimSpec() = tween<T>(
    durationMillis = MusicBarCollapsingDuration,
    easing = FastOutSlowInEasing
)

private val MusicDetailsAppearanceAnim = slideInHorizontally(
    initialOffsetX = { -it / 2 },
    animationSpec = getCommonAnimSpec()
) + fadeIn(getCommonAnimSpec())

private val MusicDetailsDisappearanceAnim = slideOutHorizontally(
    targetOffsetX = { -it / 2 },
    animationSpec = getCommonAnimSpec()
) + fadeOut(getCommonAnimSpec())

private val MusicBarAppearanceAnim = slideInVertically(
    initialOffsetY = { it / 2 },
    animationSpec = getCommonAnimSpec()
) + fadeIn(getCommonAnimSpec())

private val MusicBarDisappearanceAnim = slideOutVertically(
    targetOffsetY = { it / 2 },
    animationSpec = getCommonAnimSpec()
) + fadeOut(getCommonAnimSpec())
