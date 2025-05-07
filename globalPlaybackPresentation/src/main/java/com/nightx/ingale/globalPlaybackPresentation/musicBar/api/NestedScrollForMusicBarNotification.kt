package com.nightx.ingale.globalPlaybackPresentation.musicBar.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private class NestedScrollForMusicBarNotification(
    private val musicBarController: MusicBarController,
    private val density: Density,
) : NestedScrollConnection {

    private companion object {
        val SignificantScrollSize = 0.5.dp
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val consumedDp = density.run { consumed.y.toDp() }

        if(consumedDp < -SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.CollapseMusicBar)
        } else if (consumedDp > SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.ExpandMusicBar)
        }

        return super.onPostScroll(consumed, available, source)
    }
}

@Composable
fun rememberNestedScrollForMusicBarNotification(): NestedScrollConnection {
    val musicBarController = LocalMusicBarController.current
    val density = LocalDensity.current
    return remember {
        NestedScrollForMusicBarNotification(
            musicBarController = musicBarController,
            density = density
        )
    }
}