package com.nightx.ingale.main_navigation.musicBar

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.ingale.R
import com.nightx.ingale.core.presentation.di.ingaleViewModels
import com.nightx.ingale.core.presentation.ui.IconButton
import com.nightx.ingale.core.presentation.ui.TextMarquee
import com.nightx.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.nightx.ingale.main_navigation.musicBar.view.MusicBarViewModel
import com.nightx.ingale.ui.theme.dimensions

@SuppressLint("SuspiciousIndentation")
@Composable
fun MusicBar(
    modifier: Modifier = Modifier,
) {
    val musicBarViewModel = ingaleViewModels<MusicBarViewModel>()
    val state by musicBarViewModel.state.collectAsStateWithLifecycle()

    var maxBarWidth by remember { mutableStateOf(MusicBarHeight) }
    val cardWidth by animateDpAsState(
        targetValue = if (state.isMusicDetailsExpanded) maxBarWidth else MusicBarHeight,
        animationSpec = tween(durationMillis = MusicBarCollapsingDuration),
        label = "music bar width animation"
    )

    var marqueeContainerWidth by remember {
        mutableStateOf(0.dp)
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
            ) {
                SongIcon(
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(MaterialTheme.dimensions.normal)
                        .size(MusicIconSize)
                        .graphicsLayer {
                            this.rotationZ = musicImageRotation.value
                        },
                    model = state.currentSongInfo.currentSongThumbnail
                )
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .zIndex(0f),
                ) marqueeTextContainer@ {
                    if(marqueeContainerWidth < this.maxWidth) {
                        marqueeContainerWidth = this.maxWidth
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds(),
                        visible = state.isMusicDetailsExpanded,
                        enter = MusicDetailsAppearanceAnim,
                        exit = MusicDetailsDisappearanceAnim,
                    ) musicDetailsAnim@ {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                alignment = Alignment.CenterVertically,
                                space = MaterialTheme.dimensions.small
                            )
                        ) {
                            TextMarquee(
                                text = state.currentSongInfo.songName
                                    ?: stringResource(R.string.something_went_wrong), // this should never happen
                                modifier = Modifier.requiredWidth(marqueeContainerWidth),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            TextMarquee(
                                text = state.currentSongInfo.artistName
                                    ?: stringResource(R.string.something_went_wrong), // this should never happen
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
                    val playPauseButton = remember(state.currentSongInfo.isPlaying) {
                        if (state.currentSongInfo.isPlaying) {
                            R.drawable.ic_pause
                        } else {
                            R.drawable.ic_play
                        }
                    }
                    IconButton(
                        painter = painterResource(playPauseButton),
                        contentDescription = "stop music button",
                        onClick = musicBarViewModel::togglePlaying,
                    )
                    IconButton(
                        painter = painterResource(R.drawable.ic_arrow_next),
                        contentDescription = "stop music button",
                        onClick = musicBarViewModel::skipToNext,
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
